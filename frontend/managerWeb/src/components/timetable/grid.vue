<template>
<draggable v-model="list">
    <div class="grid">
        <template v-if="model.exit">
            <div v-if="model.first">
                删除
            </div>
            <div v-if="model.last">
                下拉
            </div>

        </template>

    </div>
</draggable>
</template>

<script>
import draggable from 'vuedraggable'
export default {
  props: ['model'],
  components: {
    draggable
  },
  data() {
    return {
      list: []
    }
  },
  created() {
    console.log(this.model)
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.grid {
    border: 1px solid #000;
    width: 100%;
    height: 50px;
}
</style>
