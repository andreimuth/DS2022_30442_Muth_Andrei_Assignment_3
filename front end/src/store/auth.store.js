import api from "../api";

const user = JSON.parse(localStorage.getItem("user"));

let initialState;
if (user) {
  initialState = { status: { userLogged: true }, user };
} else {
  initialState = { status: { userLogged: false }, user: null };
}

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    async login({ commit }, user) {
      return api.auth.login(user).then(
        (user) => {
          commit("successfulLogin", user);
          return Promise.resolve(user);
        },
        (error) => {
          alert("Invalid username or password");
          commit("failedLogin");
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }) {
      api.auth.logout();
      commit("logout");
    },
    register({ commit }, user) {
      return api.auth.register(user).then(
        (response) => {
          alert("Registration successful");
          commit("registerSuccess");
          return Promise.resolve(response.data);
        },
        (error) => {
          alert(error.response.data.message);
          commit("registerFailure");
          return Promise.reject(error);
        }
      );
    },
  },
  mutations: {
    successfulLogin(state, user) {
      state.status.userLogged = true;
      state.user = user;
    },
    failedLogin(state) {
      state.status.userLogged = false;
      state.user = null;
    },
    logout(state) {
      state.status.userLogged = false;
      state.user = null;
    },
    registerSuccess(state) {
      state.status.userLogged = false;
    },
    registerFailure(state) {
      state.status.userLogged = false;
    },
  },
  getters: {
    isAdmin: (state) => {
      console.log(state.user);
      return state.user.role === "ADMIN";
    },
  },
};
