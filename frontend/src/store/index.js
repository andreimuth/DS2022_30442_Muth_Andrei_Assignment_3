import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

import { auth } from "./auth.store";

export default new Vuex.Store({
  modules: {
    auth,
  },
});
