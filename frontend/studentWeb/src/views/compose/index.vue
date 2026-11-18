<template>
    <div>
      <free-compose v-for="(comp,index) in comps" :mname="comp.name" :mprop="comp.props" :key="index"></free-compose>
    </div>
</template>

<script>
import { getDependency } from './register'
import freeCompose from '@/components/freeCompose'
export default {
  components: {
    freeCompose
  },
  created() {
    this.getDependency()
  },
  data() {
    return {
      comps: []
    }
  },
  methods: {
    getDependency() {
      getDependency().then(e => {
        this.comps = e.data
      })
    }
  }

}
</script>

<style>

</style>
