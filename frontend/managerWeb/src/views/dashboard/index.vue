<template>
  <div class="dashboard-container" style="background-color:transparent;box-shadow:none;padding: 5px;">
    <component :is="currentRole"></component>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'
import schoolDashboard from './school'
import teacherDashboard from './teacher'

export default {
  name: 'dashboard',
  components: { adminDashboard, teacherDashboard, schoolDashboard },
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
  created() {
    if (this.roles.indexOf('school') > -1) {
      this.currentRole = 'schoolDashboard'
    } else if (this.roles.indexOf('teacher') > -1) {
      this.currentRole = 'teacherDashboard'
    } else {
      this.currentRole = 'adminDashboard'
    }
  }
}
</script>
<style scoped>

</style>

