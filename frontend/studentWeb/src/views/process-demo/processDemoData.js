const anchorTimes = [
  '2026-05-03 17:55:12',
  '2026-05-03 17:57:12',
  '2026-05-03 17:59:12',
  '2026-05-03 18:01:12',
  '2026-05-03 18:03:12',
  '2026-05-03 18:05:12',
  '2026-05-03 18:07:12',
  '2026-05-03 18:09:12',
  '2026-05-03 18:11:12',
  '2026-05-03 18:13:12',
  '2026-05-03 18:15:12',
  '2026-05-03 18:17:12',
  '2026-05-03 18:19:12',
  '2026-05-03 18:21:12',
  '2026-05-03 18:23:12',
  '2026-05-03 18:25:12',
  '2026-05-03 18:27:12',
  '2026-05-03 18:29:12',
  '2026-05-03 18:31:12'
]

const code01 = ''
const code02 = `<button class="generate-btn">生成介绍</button>`
const code03 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>`
const code04 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");`
const code05 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");
const card = document.querySelector(".profile-card");`
const code06 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");
const card = document.querySelector(".profile-card");
btn.addEventListener("click", function () {`
const code07 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");
const card = document.querySelector(".profile-card");
btn.addEventListener("click", function () {
  card.textContent = "你好，我是小明，我正在学习前端开发。";`
const code08 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");
const card = document.querySelector(".profile-card");
btn.addEventListener("click", function () {
  card.textContent = "你好，我是小明，我正在学习前端开发。";
});
</script>`
const code11 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
const btn = document.querySelector(".generate-btn");
const card = document.querySelector(".profile-card");
</script>`
const code12 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
window.onload = function () {
};
</script>`
const code13 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
window.onload = function () {
  const btn = document.querySelector(".generate-btn");
  const card = document.querySelector(".profile-card");
  btn.addEventListener("click", showProfile());
  function showProfile() {
    card.textContent = "你好，我是小明，我正在学习前端开发。";
  }
};
</script>`
const code16 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
window.onload = function () {
  const btn = document.querySelector(".generate-btn");
  const card = document.querySelector(".profile-card");
  btn.addEventListener("click", );
  function showProfile() {
    card.textContent = "你好，我是小明，我正在学习前端开发。";
  }
};
</script>`
const code17 = `<button class="generate-btn">生成介绍</button>
<div class="profile-card"></div>
<script>
window.onload = function () {
  const btn = document.querySelector(".generate-btn");
  const card = document.querySelector(".profile-card");
  btn.addEventListener("click", showProfile);
  function showProfile() {
    card.textContent = "你好，我是小明，我正在学习前端开发。";
  }
};
</script>`

const rawSteps = [
  ['view_problem', '阅读题目', code01, '', '', '', 'editing', 12],
  ['code_edit', '输入HTML第1行', code02, '', '', '', 'editing', 18],
  ['code_edit', '输入HTML第2行', code03, '', '', '', 'editing', 20],
  ['code_edit', '输入JS第1行：获取按钮元素', code04, '', '', '', 'editing', 22],
  ['code_edit', '输入JS第2行：获取卡片元素', code05, '', '', '', 'editing', 22],
  ['code_edit', '输入JS第3行：绑定点击事件', code06, '', '', '', 'editing', 26],
  ['code_edit', '输入JS第4行：设置介绍文本', code07, '', '', '', 'editing', 28],
  ['code_edit', '输入JS第5行：闭合事件函数', code08, '', '', '', 'editing', 18],
  ['run_code', '运行代码', code08, '', '', '开始运行当前代码。', 'running', 10],
  ['dom_ready_error', '错误反馈：DOM加载时机错误', code08, 'dom_ready_error', 'DOM加载时机错误，脚本执行时页面元素尚未加载完成。', '建议：使用 window.onload 或 DOMContentLoaded 包裹 DOM 查询和事件绑定代码。', 'fail', 18],
  ['debug_delete', '调试删除：删除未被加载保护包裹的事件绑定', code11, '', '', '', 'editing', 24],
  ['debug_modify', '调试修改：加入加载完成入口', code12, '', '', '', 'editing', 26],
  ['debug_modify', '调试修改：放入DOM获取和事件绑定代码', code13, '', '', '', 'editing', 40],
  ['run_code', '运行代码', code13, '', '', '开始运行加载保护后的代码。', 'running', 10],
  ['event_binding_error', '错误反馈：事件绑定错误', code13, 'event_binding_error', 'addEventListener 参数错误，showProfile() 会立即执行。', '建议：删除括号，改为 showProfile 函数引用。', 'fail', 18],
  ['debug_delete', '调试删除：删除showProfile后面的括号', code16, '', '', '', 'editing', 16],
  ['debug_modify', '调试修改：改为showProfile函数引用', code17, '', '', '', 'editing', 16],
  ['run_code', '运行成功', code17, '', '', '运行成功：点击按钮后，页面显示个人介绍内容。', 'success', 12],
  ['submit_code', '提交代码', code17, '', '', '', 'success', 10]
]

function splitLines(code) {
  return String(code || '').split('\n')
}

function buildDiff(previousSnapshot, codeSnapshot) {
  const leftLines = splitLines(previousSnapshot)
  const rightLines = splitLines(codeSnapshot)
  const max = Math.max(leftLines.length, rightLines.length)
  let addedLines = 0
  let removedLines = 0
  const blocks = []
  for (let index = 0; index < max; index++) {
    const left = leftLines[index]
    const right = rightLines[index]
    let type = 'same'
    if (left === undefined) {
      type = 'add'
      addedLines += 1
    } else if (right === undefined) {
      type = 'remove'
      removedLines += 1
    } else if (left !== right) {
      type = 'change'
      addedLines += 1
      removedLines += 1
    }
    blocks.push({
      type,
      leftNo: left === undefined ? null : index + 1,
      rightNo: right === undefined ? null : index + 1,
      left: left || '',
      right: right || ''
    })
  }
  return { blocks, addedLines, removedLines }
}

function displayErrorType(errorType) {
  const labels = {
    dom_ready_error: 'DOM 加载时机错误',
    event_binding_error: '事件绑定错误'
  }
  return labels[errorType] || errorType || ''
}

function stageOf(index) {
  if (index <= 7) return '编码输入阶段'
  if (index <= 10) return '错误定位阶段'
  if (index <= 16) return '调试修复阶段'
  return '运行提交阶段'
}

function knowledgePointsOf(step) {
  const text = `${step.actionName} ${step.errorType} ${step.codeSnapshot}`
  const tags = []
  if (text.includes('HTML')) tags.push('HTML结构')
  if (text.includes('querySelector') || text.includes('DOM')) tags.push('DOM查询')
  if (text.includes('addEventListener') || text.includes('事件')) tags.push('事件绑定')
  if (text.includes('window.onload') || text.includes('加载')) tags.push('加载时机')
  if (text.includes('showProfile')) tags.push('函数引用')
  return tags.length ? tags : ['过程记录']
}

function descriptionOf(step, diff) {
  if (step.isErrorNode) return `出现 ${displayErrorType(step.errorType)}，系统记录错误位置、信息和对应代码快照。`
  if (step.actionType === 'run_code' && step.resultStatus === 'success') return step.runOutput
  if (step.actionType === 'run_code') return '运行前代码已保存为关键快照，用于定位问题发生点。'
  if (step.actionType === 'debug_delete') return `删除关键错误写法，代码变化为 +${diff.addedLines} / -${diff.removedLines}。`
  if (step.actionType === 'debug_modify') return '这是一次报错后的调试修改，会纳入问题解决链路。'
  if (step.actionType === 'submit_code') return '提交时的最终代码已固化，形成完整证据链。'
  return `本步代码变化为 +${diff.addedLines} / -${diff.removedLines}。`
}

function evidenceCards(step, diff) {
  const cards = [
    { label: '行为类型', value: step.actionName },
    { label: '停留时间', value: `${step.duration} 秒` },
    { label: '行为标签', value: step.tags.join(' / ') },
    { label: '关联知识点', value: step.knowledgePoints.join(' / ') },
    { label: '代码变化', value: `+${diff.addedLines} / -${diff.removedLines}` }
  ]
  if (step.isErrorNode) {
    cards.splice(4, 0,
      { label: '错误类型', value: displayErrorType(step.errorType) },
      { label: '错误信息', value: step.errorMessage }
    )
  }
  return cards
}

export const processDemoTimeline = rawSteps.map((row, index) => {
  const [actionType, actionName, codeSnapshot, errorType, errorMessage, runOutput, resultStatus, duration] = row
  const previousSnapshot = index > 0 ? rawSteps[index - 1][2] : ''
  const diff = buildDiff(previousSnapshot, codeSnapshot)
  const step = {
    id: index + 1,
    step: index + 1,
    stepCode: String(index + 1).padStart(2, '0'),
    actionType,
    actionName,
    codeSnapshot,
    previousSnapshot,
    errorType,
    errorMessage,
    runOutput,
    resultStatus,
    time: anchorTimes[index],
    duration,
    language: 'javascript',
    tags: ['javascript', 'dom', 'event'],
    stage: stageOf(index),
    isErrorNode: resultStatus === 'fail' && Boolean(errorType) && errorType !== 'none',
    diffBlocks: diff.blocks,
    addedLines: diff.addedLines,
    removedLines: diff.removedLines
  }
  step.knowledgePoints = knowledgePointsOf(step)
  step.description = descriptionOf(step, diff)
  step.evidence = evidenceCards(step, diff)
  return step
})

export const processDemoErrorNodes = processDemoTimeline
  .filter((step) => step.isErrorNode)
  .map((step) => ({
    id: `error-${step.id}`,
    stepId: step.id,
    errorType: step.errorType,
    errorLabel: displayErrorType(step.errorType),
    errorMessage: step.errorMessage,
    occurredAt: step.time,
    knowledgePoints: step.knowledgePoints,
    evidenceCode: step.codeSnapshot,
    fixHint: step.errorType === 'dom_ready_error'
      ? '使用 window.onload 或 DOMContentLoaded 包裹 DOM 查询和事件绑定代码。'
      : '删除 showProfile 后面的括号，传入函数引用。'
  }))

export const processDemoDebugPaths = [
  {
    id: 'debug-1',
    title: 'DOM 加载时机错误解决链路',
    summary: '先发现脚本执行早于页面元素加载，再删除未保护的事件绑定，随后加入 window.onload 并放回 DOM 查询和事件绑定代码。',
    chain: [
      { title: '运行代码', status: 'progress', time: anchorTimes[8] },
      { title: 'DOM加载时机错误', status: 'error', time: anchorTimes[9] },
      { title: '删除未保护绑定', status: 'progress', time: anchorTimes[10] },
      { title: '加入加载入口', status: 'progress', time: anchorTimes[11] },
      { title: '重新运行', status: 'success', time: anchorTimes[13] }
    ]
  },
  {
    id: 'debug-2',
    title: '事件绑定错误解决链路',
    summary: '第二次运行发现 showProfile() 会立即执行，随后删除括号并改为 showProfile 函数引用，最终运行成功。',
    chain: [
      { title: '运行代码', status: 'progress', time: anchorTimes[13] },
      { title: '事件绑定错误', status: 'error', time: anchorTimes[14] },
      { title: '删除括号', status: 'progress', time: anchorTimes[15] },
      { title: '改为函数引用', status: 'progress', time: anchorTimes[16] },
      { title: '运行成功', status: 'success', time: anchorTimes[17] }
    ]
  }
]

export const processDemoPathNodes = [
  { id: 'view_problem', name: '阅读题目', value: 1 },
  { id: 'code_edit', name: '输入代码', value: 7 },
  { id: 'run_code', name: '运行代码', value: 3 },
  { id: 'dom_ready_error', name: 'DOM加载错误', value: 1 },
  { id: 'event_binding_error', name: '事件绑定错误', value: 1 },
  { id: 'debug_delete', name: 'debug_delete', value: 2 },
  { id: 'debug_modify', name: '调试修改', value: 3 },
  { id: 'submit_code', name: '提交代码', value: 1 }
]

export const processDemoPathLinks = [
  { source: 'view_problem', target: 'code_edit', value: 1 },
  { source: 'code_edit', target: 'run_code', value: 1 },
  { source: 'run_code', target: 'dom_ready_error', value: 1 },
  { source: 'dom_ready_error', target: 'debug_delete', value: 1 },
  { source: 'debug_delete', target: 'debug_modify', value: 1 },
  { source: 'debug_modify', target: 'run_code', value: 1 },
  { source: 'run_code', target: 'event_binding_error', value: 1 },
  { source: 'event_binding_error', target: 'debug_delete', value: 1 },
  { source: 'debug_delete', target: 'debug_modify', value: 1 },
  { source: 'debug_modify', target: 'run_code', value: 1 },
  { source: 'run_code', target: 'submit_code', value: 1 }
]

export const processDemoCounts = {
  view_problem: 1,
  code_edit: 7,
  run_code: 3,
  dom_ready_error: 1,
  event_binding_error: 1,
  debug_delete: 2,
  debug_modify: 3,
  submit_code: 1
}

export const processDemoStages = [
  { id: 'stage-1', name: '编码输入阶段', eventCount: 8, duration: 152 },
  { id: 'stage-2', name: '错误定位阶段', eventCount: 3, duration: 52 },
  { id: 'stage-3', name: '调试修复阶段', eventCount: 6, duration: 140 },
  { id: 'stage-4', name: '运行提交阶段', eventCount: 2, duration: 22 }
]

export const processDemoReport = {
  hasData: true,
  experiment: {
    id: 8,
    title: '点击按钮生成个人介绍卡片',
    language: 'javascript'
  },
  metrics: {
    eventCount: 19,
    runCount: 3,
    errorCount: 2,
    submitCount: 1,
    processScore: 86
  },
  report: {
    summary: '本次练习展示了从逐行输入 HTML 和 JS，到两次运行发现问题，再针对关键错误点删除和修正的完整闭环。',
    pattern: '细颗粒度调试型',
    latestScore: 90,
    feedbackSuggestion: '建议继续加强页面加载时机和函数引用概念，运行前先检查节点是否存在以及事件处理函数是否正确传入。'
  },
  abilityTags: ['DOM 操作', '事件监听', '调试修复', '完成闭环'],
  weakTags: ['DOM 加载时机', '事件绑定机制', '函数引用'],
  pathNodes: processDemoPathNodes,
  pathLinks: processDemoPathLinks,
  counts: processDemoCounts,
  errorInsights: [
    {
      type: 'DOM 加载时机错误',
      reason: '脚本执行时页面元素还没有准备好，按钮和卡片节点获取失败。',
      evidence: '空节点导致事件绑定失败。',
      fix: '先等待页面加载完成，再查询节点并绑定点击处理。'
    },
    {
      type: '事件绑定错误',
      reason: '把函数调用结果传给了事件绑定，点击时没有可执行的处理函数。',
      evidence: '点击按钮没有触发预期的个人介绍显示。',
      fix: '传入函数引用，不要在绑定时立即执行函数。'
    }
  ],
  recommendations: [
    {
      type: '薄弱点补强',
      title: 'DOM 加载时机专项训练',
      reason: '页面元素还没有准备好时就查询节点，容易导致后续绑定失败。',
      nextStep: '完成两道页面加载时机练习，比较不同加载入口的执行顺序。'
    },
    {
      type: '薄弱点补强',
      title: '事件绑定机制专项训练',
      reason: '函数调用和函数引用容易混淆。',
      nextStep: '练习把函数引用作为点击处理传入，观察绑定时执行和点击时执行的区别。'
    },
    {
      type: '能力强化',
      title: '调试闭环训练',
      reason: '已经具备问题定位能力，可以继续强化修改后验证的习惯。',
      nextStep: '每次运行前写下预期输出，并记录本次修改想验证什么。'
    }
  ]
}

export const processDemoEvidence = {
  hasData: true,
  summary: '这是一组独立静态演示数据：19 步行为记录、2 个错误节点、2 条调试解决链路。',
  timeline: processDemoTimeline,
  errorNodes: processDemoErrorNodes,
  debugPaths: processDemoDebugPaths,
  stages: processDemoStages
}

export function getProcessDemoStep(stepId) {
  const id = Number(stepId)
  return processDemoTimeline.find((item) => item.id === id) || processDemoTimeline[0]
}
