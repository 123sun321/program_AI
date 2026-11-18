<template>
  <div v-show="false" class="dashboard-container" style="background-color:transparent;box-shadow:none;padding: 5px;">
    <!-- <component :is="currentRole"></component> -->
    <home-top></home-top>
    <home-center></home-center>
    <home-bottom></home-bottom>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
// import adminDashboard from './admin'
// import editorDashboard from './editor'
import homeTop from './homeTop'
import homeCenter from './homeCenter'
import homeBottom from './homeBottom'

export default {
  name: 'dashboard',
  // components: { adminDashboard, editorDashboard },
  components: { homeTop, homeCenter, homeBottom },
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  beforeCreate() {
    // if (!this.roles.includes('user')) {
    //   this.currentRole = 'editorDashboard'
    // }
    this.$router.push({ name: 'mycourseindex' })
  }
}
</script>
<style scoped>

</style>

