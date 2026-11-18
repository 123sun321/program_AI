import Vue from 'vue'
import fcomp1 from '@/views/formtest/fcomp1'
import fcomp2 from '@/views/formtest/fcomp2'
import fcomp3 from '@/views/formtest/fcomp3'
Vue.component('fcom1', fcomp1)
Vue.component('fcom2', fcomp2)
Vue.component('fcom3', fcomp3)

import { fecthComposeDependency } from '@/api/compase'
export function getDependency() {
  return fecthComposeDependency({ module: 'test' })
}
