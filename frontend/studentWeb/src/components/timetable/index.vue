<template>
<div class="grid-container">
    <div class="left">
        <div>&nbsp;</div>
        <div class="grid-item" v-for="(peroid,index1) in peroids" :key="'t_'+index1">
            第{{peroid}}节
        </div>
    </div>
    <div class="right">
        <div v-for="(week,index) in weeks" :key="index">
            <div>{{week.label}}</div>
            <div class="grid-item" v-for="(peroid,index1) in peroids" :key="index+''+index1">
                <grid :model="tableMaps[week.value][peroid]"></grid>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import grid from './grid'
export default {
  components: {
    grid
  },
  props: ['peroids', 'maps'],
  data() {
    return {
      weeks: [{
        label: '星期一',
        value: 1
      },
      {
        label: '星期二',
        value: 2
      },
      {
        label: '星期三',
        value: 3
      },
      {
        label: '星期四',
        value: 4
      },
      {
        label: '星期五',
        value: 5
      },
      {
        label: '星期六',
        value: 6
      },
      {
        label: '星期七',
        value: 0
      }
      ]
    }
  },
  computed: {
    tableMaps() {
      const rtnMaps = []
      for (let i = 0; i < this.weeks.length; i++) {
        const week = this.weeks[i]
        const value = week.value
        for (let peroid = 0; peroid <= this.peroids; peroid++) {
          if (!rtnMaps[value]) { rtnMaps[value] = [] }
          const map = this.maps.find(b => b.peroid === peroid && b.week === value)
          if (map) {
            const v = this.deep(map, this.maps)
            rtnMaps[value][peroid] = v
          } else {
            rtnMaps[value][peroid] = { exit: false }
          }
        }
      }

      return rtnMaps
    }
  },
  methods: {
    deep(item, maps) {
      const v = Object.assign({}, item)
      v.exit = true
      let exit = maps.find(b => b.gcId === v.gcId && b.peroid === v.peroid + 1 && b.week === v.week)
      if (exit) {
        v.last = false
      } else {
        v.last = true
      }
      exit = maps.find(b => b.gcId === v.gcId && b.peroid === v.peroid - 1 && b.week === v.week)
      if (exit) {
        v.first = false
      } else {
        v.first = true
      }
      return v
    }
  }
}
</script>

<style lang="scss" scoped>
.grid-container {
    display: flex;
    flex-flow: row wrap;

    .right{
        display: flex;
        flex-flow: row wrap;        
    }
    .grid-item {
        width: 100px;
        height: 50px;
    }
}
</style>
