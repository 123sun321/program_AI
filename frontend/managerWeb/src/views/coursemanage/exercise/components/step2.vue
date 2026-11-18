<template>
<div class="maincontent">
        <el-form :model="entity" ref="form" label-width="120px">
            <el-row>
              <el-col :offset="6" :span="10">
                 <el-form-item prop="haveCode" label="初始数据:" :rules="[{required:true,message:'初始数据不能为空'}]">
                    <el-select v-model="entity.haveCode">
                      <el-option value="0" label="无初始数据"></el-option>
                      <el-option value="1" label="有初始数据"></el-option>
                    </el-select>
                </el-form-item>
                <div class="code_div" v-if="entity.haveCode === '1'">
                  <el-form-item label="添加资源库:">
                  <el-select @change="selectLibrary" v-model="menuSelect">
                    <el-option value="" label="--------"></el-option>
                    <el-option v-for="(item,index) in libraryList" :key="index" :value="item" :label="item.name"></el-option>
                  </el-select>
                  </el-form-item>
                  <el-form-item prop="html">
                    <el-input type="textarea" placeholder="JavaScript资源库" :rows="6" v-model="entity.codeDto.jsLibrary"></el-input>
                  </el-form-item>
                  <el-form-item prop="html">
                    <el-input type="textarea" placeholder="Style资源库" :rows="6" v-model="entity.codeDto.cssLibrary"></el-input>
                  </el-form-item>
                  <el-form-item prop="htmlConfig" label="Html源码:" style="margin-bottom:0px;">
                      <el-select v-model="entity.codeDto.htmlConfig" style="float:right;width:120px;">
                          <el-option value="html" label="HTML"></el-option>
                          <el-option value="pug" label="Pug"></el-option>
                          <el-option value="markdown" label="Markdown"></el-option>
                      </el-select>
                  </el-form-item>
                  <el-form-item prop="html">
                    <el-input type="textarea" :rows="6" v-model="entity.codeDto.html"></el-input>
                  </el-form-item>
                  <el-form-item prop="cssConfig" label="CSS源码:" style="margin-bottom:0px;">
                      <el-select v-model="entity.codeDto.cssConfig" style="float:right;width:120px;">
                          <el-option value="css" label="CSS"></el-option>
                          <el-option value="cssnext" label="cssnext"></el-option>
                          <el-option value="less" label="LESS"></el-option>
                          <el-option value="sass" label="SASS"></el-option>
                          <el-option value="scss" label="SCSS"></el-option>
                          <el-option value="stylus" label="Stylus"></el-option>
                      </el-select>
                  </el-form-item>
                  <el-form-item prop="css">
                    <el-input type="textarea" :rows="6" v-model="entity.codeDto.css"></el-input>
                  </el-form-item>
                  <el-form-item prop="jsConfig" label="JavaScript源码:" style="margin-bottom:0px;">
                      <el-select v-model="entity.codeDto.jsConfig" style="float:right;width:120px;">
                          <el-option value="js" label="JavaScript"></el-option>
                          <el-option value="babel" label="Babel"></el-option>
                          <el-option value="typescript" label="TypeScript"></el-option>
                          <el-option value="svelte" label="Svelte"></el-option>
                          <el-option value="reason" label="Reason"></el-option>
                          <el-option value="coffeescript-2" label="CoffeeScript 2"></el-option>
                          <el-option value="rust" label="Rust"></el-option>
                      </el-select>
                  </el-form-item>
                  <el-form-item prop="js">
                    <el-input type="textarea" :rows="6" v-model="entity.codeDto.js"></el-input>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
        </el-form>
        <div style="text-align:center;padding-top:10px;">
          <el-button  @click="onBack">上一步</el-button>
          <el-button type="primary" @click="onNext">下一步</el-button>
        </div>
</div>
</template>

<script>
/**
 * @file 实验初始数据
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import { list } from '@/api/base/baseLibrary'
export default {
  props: ['entity'],
  data() {
    return {
      libraryList: [],
      menuSelect: ''
    }
  },
  created() {
    this.$nextTick(e => {
      list().then(e => {
        this.libraryList = e.data.data
      })
    })
  },
  methods: {
    selectLibrary(item) {
      if (item.jsLibrary) {
        this.entity.codeDto.jsLibrary = this.entity.codeDto.jsLibrary ? this.entity.codeDto.jsLibrary + '\n' + item.jsLibrary : item.jsLibrary
      }
      if (item.cssLibrary) {
        this.entity.codeDto.cssLibrary = this.entity.codeDto.cssLibrary ? this.entity.codeDto.cssLibrary + '\n' + item.cssLibrary : item.cssLibrary
      }
      this.menuSelect = ''
    },
    onBack() {
      this.$emit('back')
    },
    onNext() {
      this.$refs.form.validate(e => {
        if (e) {
          this.$emit('next')
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-textarea textarea{height: 200px;}
</style>
