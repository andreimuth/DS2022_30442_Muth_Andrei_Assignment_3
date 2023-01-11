<template>
  <v-container>
    <v-layout row justify-center pt-3>
      <v-flex xs4 class="grey lighten-4">
        <v-container class="text-xs-center">
          <v-card-title>
            <h4 v-if="loginMode">Login</h4>
            <h4 v-else>Register</h4>
          </v-card-title>
          <v-form>
            <v-text-field
              prepend-icon="person"
              name="Username"
              label="Username"
              v-model="login.username"
              validate-on-blur
            ></v-text-field>
            <v-text-field
              prepend-icon="lock"
              name="Password"
              label="Password"
              type="password"
              v-model="login.password"
              validate-on-blur
            ></v-text-field>
            <v-card-actions>
              <v-container>
                <v-layout v-if="loginMode" row justify-center>
                  <v-btn plain @click="attemptLogin">Login</v-btn>
                  <v-btn plain @click="switchMode">
                    Click here to register!
                  </v-btn>
                </v-layout>

                <v-layout v-else row justify-center>
                  <v-btn plain @click="attemptRegister">Register</v-btn>
                  <v-btn plain @click="switchMode">
                    Click here to login!
                  </v-btn>
                </v-layout>
                <div
                  v-if="!loginMode"
                  style="color: darkgrey"
                  class="move-down"
                >
                  The password must contain:
                  <ul>
                    <li v-if="!has8Characters" class="li-red">
                      At least 8 characters
                    </li>
                    <li v-else class="li-green">At least 8 characters</li>
                    <li v-if="!hasUppercaseLetter" class="li-red">
                      At least one uppercase letter
                    </li>
                    <li v-else class="li-green">
                      At least one uppercase letter
                    </li>
                    <li v-if="!hasLowercaseLetter" class="li-red">
                      At least one lowercase letter
                    </li>
                    <li v-else class="li-green">
                      At least one lowercase letter
                    </li>
                    <li v-if="!hasNumber" class="li-red">
                      At least one number
                    </li>
                    <li v-else class="li-green">At least one number</li>
                    <li v-if="!hasSpecialCharacter" class="li-red">
                      At least one special character
                    </li>
                    <li v-else class="li-green">
                      At least one special character
                    </li>
                  </ul>
                </div>
              </v-container>
            </v-card-actions>
          </v-form>
        </v-container>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import router from "../router";
export default {
  name: "HelloWorld",
  data: () => ({
    mode: "login",
    login: {
      username: "",
      password: "",
    },
  }),
  methods: {
    attemptLogin() {
      this.$store.dispatch("auth/login", this.login).then(() => {
        if (this.$store.state.auth.status.userLogged) {
          if (this.$store.getters["auth/isAdmin"]) {
            router.push("/admin");
          } else {
            router.push("/user");
          }
        }
      });
    },
    async attemptRegister() {
      await this.$store.dispatch("auth/register", this.login);
    },
    switchMode() {
      this.mode = this.mode === "login" ? "register" : "login";
    },
    logout() {
      this.$store.dispatch("auth/logout");
    },
  },
  computed: {
    loginMode: function () {
      return this.mode === "login";
    },
    has8Characters() {
      return this.login.password.length >= 8;
    },
    hasUppercaseLetter() {
      return /[A-Z]/.test(this.login.password);
    },
    hasLowercaseLetter() {
      return /[a-z]/.test(this.login.password);
    },
    hasNumber() {
      return /\d/.test(this.login.password);
    },
    hasSpecialCharacter() {
      return /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(this.login.password);
    },
  },
};
</script>

<style>
.move-down {
  padding-top: 20px;
  font-family: Helvetica, serif;
}

.li-red {
  color: #de3163;
}

.li-green {
  color: #097969;
}
</style>
