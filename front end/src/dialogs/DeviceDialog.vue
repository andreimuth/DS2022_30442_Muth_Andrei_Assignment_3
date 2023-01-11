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
          <v-text-field label="Name" v-model="device.name" />
          <v-text-field label="Description" v-model="device.description" />
          <v-text-field label="Address" v-model="device.address" />
          <v-text-field
            type="number"
            min="0"
            label="Maximum hourly energy consumption"
            v-model="device.maximumHourlyEnergyConsumption"
          />
          <v-select
            label="Owner username"
            v-model="device.ownerUsername"
            :items="usernames"
          ></v-select>
          <v-btn v-if="isNew" @click="create">Create device</v-btn>
          <v-btn v-else @click="update">Update device</v-btn>
          <v-btn @click="closeDialog">Close dialog</v-btn>
        </v-form>
      </v-card>
    </template>
  </v-dialog>
</template>

<script>
import api from "@/api";

export default {
  name: "DeviceDialog",
  props: {
    opened: Boolean,
    device: Object,
    usernames: [],
  },
  data() {
    return {};
  },
  methods: {
    create() {
      api.devices
        .create({
          name: this.device.name,
          description: this.device.description,
          address: this.device.address,
          maximumHourlyEnergyConsumption: parseFloat(
            this.device.maximumHourlyEnergyConsumption
          ),
          ownerUsername: this.device.ownerUsername,
        })
        .then(
          () => this.$emit("refresh"),
          (error) => {
            alert(error.response.data);
          }
        );
    },
    update() {
      api.devices
        .update(this.device.id, {
          name: this.device.name,
          description: this.device.description,
          address: this.device.address,
          maximumHourlyEnergyConsumption: parseFloat(
            this.device.maximumHourlyEnergyConsumption
          ),
          ownerUsername: this.device.ownerUsername,
        })
        .then(
          () => this.$emit("refresh"),
          (error) => {
            alert(error.response.data);
          }
        );
    },
    closeDialog() {
      this.$emit("refresh");
    },
    takeUsername(user) {
      this.usernames.push(user.username);
    },
  },
  async created() {
    const users = await api.users.findNonAdminUsers();
    users.forEach(this.takeUsername);
  },
  computed: {
    isNew: function () {
      return !this.device.id;
    },
  },
};
</script>
