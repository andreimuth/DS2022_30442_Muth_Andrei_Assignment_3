import Vue from "vue";
import VueRouter from "vue-router";
import Login from "@/views/Login";
import AdminView from "@/views/AdminView";
import UserView from "@/views/UserView";
import { auth as store } from "../store/auth.store";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Login",
    component: Login,
    beforeEnter: (to, from, next) => {
      const user = store.state.user;
      if (user == null) {
        next();
      } else {
        if (user.role === "ADMIN") {
          next("/admin");
        } else {
          next("/user");
        }
      }
    },
  },
  {
    path: "/user",
    name: "User",
    component: UserView,
    beforeEnter: (to, from, next) => {
      const user = store.state.user;
      if (user != null && user.role === "USER") {
        next();
      } else {
        next(false);
      }
    },
  },
  {
    path: "/admin",
    name: "Admin",
    component: AdminView,
    beforeEnter: (to, from, next) => {
      const user = store.state.user;
      if (user != null && user.role === "ADMIN") {
        next();
      } else {
        next(false);
      }
    },
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

export default router;
