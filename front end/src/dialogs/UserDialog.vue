<template>
  <v-dialog
    transition="dialog-bottom-transition"
    max-width="500"
    :value="opened"
    persistent
  >
    <template>
      <v-card>
        <v-form>
          <v-text-field label="Username" v-model="user.username" />
          <v-text-field
            label="Password"
            v-model="user.password"
            type="password"
          />
          <v-select label="Role" v-model="user.role" :items="roles"></v-select>
          <v-btn v-if="isNew" @click="create">Create new user</v-btn>
          <v-btn v-else @click="update">Update user</v-btn>
          <v-btn @click="closeDialog">Close dialog</v-btn>
        </v-form>
      </v-card>
    </template>
  </v-dialog>
</template>

<script>
import api from "@/api";

export default {
  name: "UserDialog",
  props: {
    opened: Boolean,
    user: Object,
  },
  data() {
    return {
      roles: [],
    };
  },
  methods: {
    create() {
      api.users
        .create({
          username: this.user.username,
          password: this.user.password,
          role: this.user.role,
        })
        .then(
          () => this.$emit("refresh"),
          (error) => {
            alert(error.response.data);
          }
        );
    },
    update() {
      api.users
        .update(this.user.id, {
          username: this.user.username,
          password: this.user.password,
          role: this.user.role,
        })
        .then(
          () => {
            const loggedUser = JSON.parse(localStorage.getItem("user"));
            if (this.user.id === loggedUser.id) {
              this.$store.dispatch("auth/logout");
              this.$router.push("/");
            } else {
              this.$emit("refresh");
            }
          },
          (error) => {
            alert(error.response.data);
          }
        );
    },
    closeDialog() {
      this.$emit("refresh");
    },
  },
  async created() {
    this.roles = await api.roles.getRoles();
  },
  computed: {
    isNew() {
      return !this.user.id;
    },
  },
};
</script>
