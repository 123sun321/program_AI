package com.cube001.cubeboot.service.assignment.impl;

import com.baomidou.mybatisplus.annotations.SqlParser;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;

import com.cube001.cubeboot.service.assignment.dto.AssignmentSubmitDto;
import com.cube001.cubeboot.service.assignment.dto.GetCountAssignmentSubmitDto;
import com.cube001.cubeboot.service.assignment.dto.MyWebSocket;
import com.cube001.cubeboot.service.assignment.dto.diff_match_patch;
import com.cube001.cubeboot.service.assignment.mapper.AssignmentMapper;
import com.cube001.cubeboot.service.assignment.mapper.AssignmentSubmitMapper;
import com.cube001.cubeboot.service.assignment.mapper.RecommendWorkMapper;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.mapper.CodeMapper;
import com.cube001.cubeboot.service.training.mapper.CodeSimilarityMapper;
import com.cube001.cubeboot.service.training.mapper.TrainingMapper;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.training.entity.CodeSimilarity;
import com.cube001.cubeboot.training.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  作业提交类服务实现类
 * </p>
 *
 * @author renjie
 * @since 2019-03-22
 */
@Service
public class AssignmentSubmitServiceImpl extends ServiceImpl<AssignmentSubmitMapper, AssignmentSubmit> implements AssignmentSubmitService {


    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private RecommendWorkMapper recommendWorkMapper;
    @Autowired
    private TrainingMapper trainingMapper;

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private  MyWebSocket webSocket;
    @Autowired
    private CodeSimilarityMapper codeSimilarityMapper;

    /**
      * @description分页/模糊查询
      * @author 张人杰
      * @date 2025/3/28 9:05
      * @Param
      * @return
      */

    @Override
    public Page<AssignmentSubmit> getAssignmentSubmitList(Page<AssignmentSubmit> page,Long id,Long courseId , Wrapper<AssignmentSubmit> formToEntityWrapperWithSearch) {
        List<AssignmentSubmit> stuRewardPunishList = this.baseMapper.getAssignmentList(page,id,courseId, formToEntityWrapperWithSearch.eq("am.is_deleted",0).orderBy("ams.score",false));
        page.setRecords(stuRewardPunishList);
        return page;
    }
    /**
      * @description作业提交评分的设定以及作业中未评分人数的 减1
      * @author 张人杰
      * @date 2025/3/28 9:06
      * @Param Long assignmentSubmitid作业提交Id Integer Score作业提交评分
      * @return
      */

    @Override
    public void editorScore(Long assignmentSubmitid, Integer Score) {
        AssignmentSubmit assignmentSubmitById = baseMapper.selectById(assignmentSubmitid);
        assignmentSubmitById.setId(assignmentSubmitid);
        assignmentSubmitById.setScore(Score);


        if (!assignmentSubmitById.getWhetherScore()){
            assignmentSubmitById.setWhetherScore(true);
            assignmentMapper.editeNoScore(assignmentSubmitById.getAssignmentId());
            baseMapper.updateById(assignmentSubmitById);
        }else{
            baseMapper.updateById(assignmentSubmitById);
        }

    }
    /**
      * @description作业提交中 把学生的作业设置为推荐作品
      * @author 张人杰
      * @date 2025/3/28 9:08
      * @Param Long assignmentSubmitid作业提交Id, int Recommend是否设为推荐
      * @return
      */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editorRecommend(Long assignmentSubmitid) {
        //通过提交作业ID获取里面的字段
        String name = ShiroKit.getUser().getName();
        AssignmentSubmit assignmentSubmit = baseMapper.selectById(assignmentSubmitid);
        RecommendWork recommendWork =new RecommendWork();
        //通过作业ID获取标题名称
        recommendWork.setTitle(assignmentMapper.selectById(assignmentSubmit.getAssignmentId()).getTitle());
        //通过提交作业ID获取学生ID
        recommendWork.setStudentId(assignmentSubmit.getStudentId());
        recommendWork.setAssignmentSubmitId(assignmentSubmitid);
        recommendWork.setRecommenderName(name);
        recommendWork.setCodeId(trainingMapper.selectById(assignmentSubmit.getTrainingId()).getCodeId());
        Integer insert = recommendWorkMapper.insert(recommendWork);
        if (insert>0){
            assignmentSubmit.setRecommend(1);
            baseMapper.updateById(assignmentSubmit);
        }else {
            throw  new FriendException("推荐失败");
        }

    }
    /**
      * @description 取消推荐
      * @author 张人杰
      * @date 2025/5/15 14:34
      * @Param Long assignmentSubmitid
      * @return
      */

    @Override
    public void editordelRecommend(Long assignmentSubmitid) {
        AssignmentSubmit assignmentSubmit = baseMapper.selectById(assignmentSubmitid);
        EntityWrapper<RecommendWork> recommendWorkEntityWrapper=new EntityWrapper<>();
        recommendWorkEntityWrapper.eq("assignment_submit_id",assignmentSubmitid);
        Integer delete = recommendWorkMapper.delete(recommendWorkEntityWrapper);
        if (delete>0){
            assignmentSubmit.setRecommend(0);
            baseMapper.updateById(assignmentSubmit);
        }else {
            throw  new FriendException("取消推荐失败");
        }


    }

    @Override
    public GetCountAssignmentSubmitDto getByassignmentId(Long assignmentId,Long courseid,Long gradeid,Long groupid) {
        Wrapper<AssignmentSubmit> assignmentSubmitWrapper=new EntityWrapper<>();
        assignmentSubmitWrapper.eq("am.id",assignmentId);
        if (gradeid!=null){
            assignmentSubmitWrapper.eq("bs.grade_id",gradeid);
        }else if (groupid!=null){
            assignmentSubmitWrapper.eq("cj.course_group_id",groupid);
        }
        GetCountAssignmentSubmitDto assignmentId1 = baseMapper.getAssignmentId(assignmentId,courseid,assignmentSubmitWrapper);
        return assignmentId1;
    }

    /**
     * @description  获取所有为评分的作业列表
     * @author 马勇超
     * @date 2025/4/8 14:11
     * @return   list
     */
    @Override
    public List<AssignmentSubmit> getNoScoreList() {
        List<AssignmentSubmit> submits = baseMapper.getNoScoreList();
        return submits;
    }
    /**
     * 通过编号获取作业提交详情及代码
     * @author Juci
     * @param id
     * @return
     */
    @Override
    public AssignmentSubmitDto getEntityWithCode(Long id){
        return this.baseMapper.getEntityWithCode(id);
    }

    @Async
    @Override
    public String gettest1(List<Long> ids,String tenantId) {


        for (Long id:ids
             ) {
            Object building = new Object();
            diff_match_patch dmp=new diff_match_patch();
            String result=null;
            List<Code> codeList=null;
            List<AssignmentSubmit> assignmentSubmits=null;
            Code textcode=null;
            double lastResult=0;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            synchronized (building){
                try {
                    try(TenantFliter tenantFliter=new TenantFliter(true,tenantId)) {
                        if (id==null){
                            continue;
                        }else {
//                            List<Long> ids=assignmentResources.stream().map(m->m.getId()).collect(Collectors.toList());

                            assignmentSubmits = this.baseMapper.selectBatchIds(ids);
                            //获取所有练习的id
//                            List<Long> trainingID = assignmentSubmits.stream().map(m -> m.getTrainingId()).collect(Collectors.toList());
//                            List<Training> trainings = trainingMapper.selectBatchIds(trainingID);
                            //获取所有代码的id
//                            List<Long> CodeID = trainings.stream().map(m -> m.getCodeId()).collect(Collectors.toList());
                            //1.单个作业提交ID
                            AssignmentSubmit assignmentSubmit = this.baseMapper.selectById(id);
                            //2.单个联系的ID
                            Training training = trainingMapper.selectById(assignmentSubmit.getTrainingId());
                            //3.单个练习的ID查询
                            textcode = codeMapper.selectById(training.getCodeId());
                           //4.获取已提交的所有代码
//                            codeList = codeMapper.selectBatchIds(CodeID);
                        }

                    }


                    for (AssignmentSubmit Submit: assignmentSubmits) {
                        Code code;
                        try(TenantFliter tenantFliter=new TenantFliter(true,tenantId)) {
                            Training training = trainingMapper.selectById(Submit.getTrainingId());
                            //3.单个练习的ID查询

                            code = codeMapper.selectById(training.getCodeId());
                        }
                        if (textcode.getId().equals(code.getId())){
                                continue;
                            }else {
                                int html = dmp.diff_levenshtein(dmp.diff_main(textcode.getHtml(), code.getHtml(), false));
                                int js = dmp.diff_levenshtein(dmp.diff_main(textcode.getJs(), code.getJs(), false));
                                int css = dmp.diff_levenshtein(dmp.diff_main(textcode.getCss(), code.getCss(), false));
                                //所有不相同的字符串
                                int text1=html+js+css;
                                //提交者的代码总长度
                                int text2=textcode.getHtml().length()+textcode.getJs().length()+textcode.getCss().length();
                                //对比者的代码总长度
                                int text3=code.getHtml().length()+code.getJs().length()+code.getCss().length();
                                if (text2>text3){
                                    result = numberFormat.format((double)(text2-text1)  / (double)text2 * 100);
                                    System.out.println(result);

                                }else {
                                    result =numberFormat.format((double)(text3-text1)  / (double)text3 * 100);
                                    System.out.println(result);
                                }
                                //创建代码相似度实体类对象并插入数据库
                                CodeSimilarity codeSimilarity=new CodeSimilarity();
                                codeSimilarity.setSubmitterWorkId(id);
                                codeSimilarity.setComparatorWorkId(Submit.getId());
                                codeSimilarity.setStudentId(Submit.getStudentId());
                                codeSimilarity.setAssignmentId(Submit.getAssignmentId());
                                codeSimilarity.setSimilarity(result);
                                //根据时间
                                if (textcode.getCreationTime().getTime()>code.getCreationTime().getTime()&&Double.parseDouble(result)>50.00)
                                 codeSimilarity.setPlagiarize(true);
                                Wrapper<CodeSimilarity> codeSimilarityWrapper=new EntityWrapper<>();
                                try(TenantFliter tenantFliter=new TenantFliter(true,tenantId)) {
                                    codeSimilarityWrapper.eq("submitter_work_id",id).eq("comparator_work_id",Submit.getId()).eq("assignment_id",Submit.getAssignmentId());
                                    //删除之前对的相似度记录
                                    codeSimilarityMapper.delete(codeSimilarityWrapper);
                                    //重新插入新的对比相似度记录
                                    codeSimilarityMapper.insert(codeSimilarity);
                                }
                                if (Double.parseDouble(result)>lastResult){

                                    lastResult=Double.parseDouble(result);
                                }
                                AssignmentSubmit assignmentSubmit=new AssignmentSubmit();
                                assignmentSubmit.setId(id);
                                assignmentSubmit.setCodeSimilarity(lastResult+"");
                                try(TenantFliter tenantFliter=new TenantFliter(true,tenantId)) {
                                    this.baseMapper.updateById(assignmentSubmit);
                                    Assignment assignment=new Assignment();
                                    assignment.setId(Submit.getAssignmentId());
                                    assignment.setDuplicateChecking(new Date());
                                    this.assignmentMapper.updateById(assignment);
                                }
//                                webSocket.sendOneMessage("DPS007", "1");
//                                System.out.println(lastResult+"结果*********************");
                            }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        webSocket.sendOneMessage("DPS007", "1");
        return Thread.currentThread().getName()+"执行完毕";

    }

    /**
      * @description 根据提交作业id获取作业和学生信息
      * @author 马勇超
      * @date 2025/7/4 15:36
      * @param id
      * @return
     */
    @Override
    public AssignmentSubmit getStudentAndAssignmentById(Long id) {
        return baseMapper.getStudentAndAssignmentById(id);
    }
}
