/**
 * Created by jiachenpan on 16/11/18.
 */

export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function formatTime(time, option) {
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) { // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
  }
}

export function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (const k in o) {
    if (new RegExp(`(${k})`).test(fmt)) {
      const str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}
function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}

// 格式化时间
export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 *get getByteLen
 * @param {Sting} val input value
 * @returns {number} output value
 */
export function getByteLen(val) {
  let len = 0
  for (let i = 0; i < val.length; i++) {
    if (val[i].match(/[^\x00-\xff]/ig) != null) {
      len += 1
    } else { len += 0.5 }
  }
  return Math.floor(len)
}

export function cleanArray(actual) {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

export function param(json) {
  if (!json) return ''
  return cleanArray(Object.keys(json).map(key => {
    if (json[key] === undefined) return ''
    return encodeURIComponent(key) + '=' +
            encodeURIComponent(json[key])
  })).join('&')
}

export function param2Obj(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}')
}

export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

export function objectMerge(target, source) {
  /* Merges two  objects,
     giving the last one precedence */

  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach((property) => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

export function scrollTo(element, to, duration) {
  if (duration <= 0) return
  const difference = to - element.scrollTop
  const perTick = difference / duration * 10
  setTimeout(() => {
    console.log(new Date())
    element.scrollTop = element.scrollTop + perTick
    if (element.scrollTop === to) return
    scrollTo(element, to, duration - 10)
  }, 10)
}

export function toggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString = classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

export const pickerOptions = [
  {
    text: '今天',
    onClick(picker) {
      const end = new Date()
      const start = new Date(new Date().toDateString())
      end.setTime(start.getTime())
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近一周',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(end.getTime() - 3600 * 1000 * 24 * 7)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      picker.$emit('pick', [start, end])
    }
  }, {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      picker.$emit('pick', [start, end])
    }
  }]

export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

export function debounce(func, wait, immediate) {
  let timeout, args, context, timestamp, result

  const later = function() {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔last小于设定时间间隔wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

export function deepClone(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'shallowClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach((keys) => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = source[keys].constructor === Array ? [] : {}
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

// 深拷贝
export function deepCopy(obj) {
  var object
  if (Object.prototype.toString.call(obj) === '[object Array]') {
    object = []
    for (var i = 0; i < obj.length; i++) {
      object.push(deepCopy(obj[i]))
    }
    return object
  }

  if (Object.prototype.toString.call(obj) === '[object Object]') {
    object = {}
    for (var p in obj) {
      object[p] = obj[p]
    }
    return object
  }
}

// 普通Json数组转层级Json数组
export function arrayToJson(treeArray) {
  var r = []
  var tmpMap = {}

  for (var i = 0, l = treeArray.length; i < l; i++) {
    // 以每条数据的id作为obj的key值，数据作为value值存入到一个临时对象里面
    tmpMap[treeArray[i]['id']] = treeArray[i]
  }

  for (i = 0, l = treeArray.length; i < l; i++) {
    var key = tmpMap[treeArray[i]['parentId']]

    // 循环每一条数据的pid，假如这个临时对象有这个key值，就代表这个key对应的数据有children，需要Push进去
    if (key) {
      if (!key['children']) {
        key['children'] = []
        key['children'].push(treeArray[i])
      } else {
        key['children'].push(treeArray[i])
      }
    } else {
    // 如果没有这个Key值，那就代表没有父级,直接放在最外层
      r.push(treeArray[i])
    }
  }
  return r
}

export function numToCn(month) {
  const chinese = ['一', '二', '三', '四', '五', '六', '七', '八', '九']
  if (month.length === 2) {
    if (month.charAt(0) === '1') {
      return '十' + chinese[month.charAt(1)]
    }
  } else {
    return chinese[month.charAt(0)]
  }
}

/**
 * 是否为Null
 * @param object
 * @returns {Boolean}
 */
export function isNull(object) {
  if (object == null || typeof object === 'undefined') {
    return true
  }
  return false
}

/**
 * 根据日期字符串获取星期几
 * @param dateString 日期字符串（如：2016-12-29），为空时为用户电脑当前日期
 * @returns {String}
 */
export function getWeek(dateString) {
  var date
  if (isNull(dateString)) {
    date = new Date()
  } else {
    var dateArray = dateString.split(' ')[0].split('-')
    date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2])
  }
  return '周' + '日一二三四五六'.charAt(date.getDay())
}

// js对象数组(JSON) 根据某个共同字段 分组
export function jsonToGroup(arr) {
  const map = {}
  const dest = []
  for (var i = 0; i < arr.length; i++) {
    var ai = arr[i]
    const vmonth = ai.creationTime.substr(0, 7)
    if (!map[vmonth]) {
      dest.push({
        creationTime: vmonth,
        data: [ai]
      })
      map[vmonth] = ai
    } else {
      for (var j = 0; j < dest.length; j++) {
        var dj = dest[j]
        if (dj.creationTime.substr(0, 7) === ai.creationTime.substr(0, 7)) {
          dj.data.push(ai)
          break
        }
      }
    }
  }
  return dest
}

// 从树形菜单中移除指定菜单项
export function removeNodeFromTrees(nodeIds, treeNodes) {
  for (var i = 0; i < treeNodes.length; i++) {
    if (nodeIds.findIndex(id => id === treeNodes[i].id) === -1) {
      if (treeNodes[i].children) {
        removeNodeFromTrees(nodeIds, treeNodes[i].children)
      }
    } else {
      treeNodes.splice(i, 1)
      i = -1
    }
  }
  return treeNodes
}
// 获取子地区
export function getSubAreaTree(arrAreaList, id, arrTree = []) {
  arrAreaList.forEach((value, key) => {
    if (value.parentId === id) {
      arrTree.push(value)
      getSubAreaTree(arrAreaList, value.id, arrTree)
    }
  })
  return arrTree
}

// 树形转线性
export function treeConvertList(root) {
  const list = []
  if (root) {
    const Root = JSON.parse(JSON.stringify(root))
    const queue = [...Root]
    while (queue.length) {
      const node = queue.shift()
      if (node.children && node.children.length) {
        queue.push(...node.children)
      }
      delete node.children
      if (node.parentId !== null && node.parentId !== undefined) {
        // 查找过滤重复添加
        if (list.length === 0) {
          list.push(node)
        } else if (list.length > 0 && list.findIndex((item) => item.id === node.id) === -1) {
          list.push(node)
        }
      }
    }
  }
  return list
}

// 线性转树形
export function listConvertTree(list) {
  let root = null
  if (list && list.length) {
    root = { id: 0, parentId: null, children: [] }
    const group = {}
    for (let index = 0; index < list.length; index += 1) {
      if (list[index].parentId !== null && list[index].parentId !== undefined) {
        if (!group[list[index].parentId]) {
          group[list[index].parentId] = []
        }
        group[list[index].parentId].push(list[index])
      }
    }
    const queue = []
    queue.push(root)
    while (queue.length) {
      const node = queue.shift()
      node.children = group[node.id] && group[node.id].length ? group[node.id] : null
      if (node.children) {
        queue.push(...node.children)
      }
    }
  }
  return root.children
}

// 选择查找自身、下级、父级地区
export function selectSelfAndParent(areaIds, areaList) {
  const tempArea = []
  areaIds.forEach(function(id) {
    let selfArea = areaList.find((item) => item.id === id) // 根据id获取自身
    if (selfArea) {
      tempArea.push(selfArea) // 添加自身
    }
    // 添加上级地区到数组
    while (selfArea) {
      selfArea = areaList.find((item) => item.id === selfArea.parentId)
      if (selfArea && tempArea.findIndex((item) => item.id === selfArea.id) === -1) {
        tempArea.push(selfArea)
      }
    }
    // 添加下级地区到数组
    const subArea = getSubAreaTree(areaList, id)
    tempArea.push(...subArea)
  })

  // 数组去重
  const hash = {}
  const newArr = tempArea.reduceRight((item, next) => {
    hash[next.id] ? '' : hash[next.id] = true && item.push(next)
    return item
  }, [])
  return listConvertTree(newArr)
}

/**
 *添加可选地区项到已选地区中
 * @param {addArea} val input value
 * @param {enableTreeArea} val input value
 * @param {selectedTreeArea} val input value
 * @returns {newSelectedTreeArea} output value
 */
export function addEnableToSelectedArea(addArea, enableTreeArea, selectedTreeArea) {
  let enableAreaList = []
  let selectedAreaList = []
  addArea = treeConvertList(addArea)
  enableAreaList = treeConvertList(enableTreeArea) // 线性化可选地区
  selectedAreaList = treeConvertList(selectedTreeArea) // 线性化已选地区

  // 查找选中地区的父级地区
  const tempArea = []
  addArea.forEach(function(value) {
    let area = enableAreaList.find((item) => item.id === value.parentId)
    if (area && addArea.findIndex((a) => a.id === area.id) === -1) {
      tempArea.push(area)
      while (area) {
        area = enableAreaList.find((item) => item.id === area.parentId)
        if (area && tempArea.findIndex((item) => item.id === area.id) === -1) {
          tempArea.push(area)
        }
      }
    }
  })
  // 合并数组选中地区(addArea)和上级地区(tempArea)到选中地区(addArea)
  addArea.unshift.apply(addArea, tempArea)
  // 合并选中地区(addArea)和已选中地区(selectedAreaList)到已选中地区(selectedAreaList)
  selectedAreaList.unshift.apply(selectedAreaList, addArea)
  // 数组去重
  const hash = {}
  const newArr = selectedAreaList.reduceRight((item, next) => {
    hash[next.id] ? '' : hash[next.id] = true && item.push(next)
    return item
  }, [])

  return listConvertTree(newArr)
}

export function convertArea(selectedAreas) {
  var areaIds = []
  var count = 0
  var areaName = ''

  for (let i = 0; i < selectedAreas.length; i++) { // 循环省份
    count = 0
    var cityIds = []
    var cityName = ''
    for (let j = 0; j < selectedAreas[i].children.length; j++) { // 循环市
      // 判断城市是否选中所有区
      // var cityIds = []
      // var cityName = ''
      if (!selectedAreas[i].children[j].children || selectedAreas[i].children[j].count === selectedAreas[i].children[j].children.length) {
        count++
        cityName += selectedAreas[i].children[j].label + '、'
        cityIds.push(selectedAreas[i].children[j].id)
      } else {
        cityName += selectedAreas[i].children[j].label + '('
        for (let k = 0; k < selectedAreas[i].children[j].children.length; k++) {
          cityIds.push(selectedAreas[i].children[j].children[k].id)
          cityName += selectedAreas[i].children[j].children[k].label + '、'
        }
        cityName = cityName.substr(0, cityName.length - 1)
        cityName += ')、'
      }
      // cityName = cityName.substr(0, cityName.length - 1)
    }
    cityName = cityName.substr(0, cityName.length - 1)
    if (selectedAreas[i].count === count) {
      areaIds.push(selectedAreas[i].id)
      areaName += selectedAreas[i].label + '、'
    } else {
      areaIds.unshift.apply(areaIds, cityIds) // 合并数组
      areaName += cityName + '、'
    }
  }
  // console.log(areaName)
  return { areaLabels: areaName.substr(0, areaName.length - 1), areaIds: areaIds.join() }
}

// 构造级联分类列表
export function formatCategoryData(treeArray) {
  var r = []
  var tmpMap = {}
  treeArray = treeArray.map(function(data) {
    return {
      value: data.id,
      label: data.name,
      parentValue: data.parentId
    }
  })

  for (var i = 0, l = treeArray.length; i < l; i++) {
    // 以每条数据的id作为obj的key值，数据作为value值存入到一个临时对象里面
    tmpMap[treeArray[i]['value']] = treeArray[i]
  }

  for (i = 0, l = treeArray.length; i < l; i++) {
    var key = tmpMap[treeArray[i]['parentValue']]

    // 循环每一条数据的pid，假如这个临时对象有这个key值，就代表这个key对应的数据有children，需要Push进去
    if (key) {
      if (!key['children']) {
        key['children'] = []
        key['children'].push(treeArray[i])
      } else {
        key['children'].push(treeArray[i])
      }
    } else {
    // 如果没有这个Key值，那就代表没有父级,直接放在最外层
      r.push(treeArray[i])
    }
  }
  return r
}

// 构造地区级联分类列表
export function formatAreaData(treeArray) {
  var r = []
  var tmpMap = {}
  treeArray = treeArray.map(function(data) {
    return {
      value: data.id,
      label: data.label,
      parentValue: data.parentId
    }
  })

  for (var i = 0, l = treeArray.length; i < l; i++) {
    // 以每条数据的id作为obj的key值，数据作为value值存入到一个临时对象里面
    tmpMap[treeArray[i]['value']] = treeArray[i]
  }

  for (i = 0, l = treeArray.length; i < l; i++) {
    var key = tmpMap[treeArray[i]['parentValue']]

    // 循环每一条数据的pid，假如这个临时对象有这个key值，就代表这个key对应的数据有children，需要Push进去
    if (key) {
      if (!key['children']) {
        key['children'] = []
        key['children'].push(treeArray[i])
      } else {
        key['children'].push(treeArray[i])
      }
    } else {
    // 如果没有这个Key值，那就代表没有父级,直接放在最外层
      r.push(treeArray[i])
    }
  }
  return r
}
