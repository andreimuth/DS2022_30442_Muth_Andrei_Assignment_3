<template>
  <div>
    <v-btn
      @click="
        $store.dispatch('auth/logout');
        $router.push('/');
      "
      plain
      ><v-icon>mdi-arrow-left</v-icon>Log out</v-btn
    >
    <div>
      <h2>Users</h2>
      <v-btn @click="addUser" plain>Create new user</v-btn>
      <v-data-table :headers="userHeaders" :items="users">
        <template v-slot:item="row">
          <tr @click="updateUser(row.item)">
            <td>{{ row.item.username }}</td>
            <td>{{ row.item.role }}</td>
            <td>
              <v-btn
                title="Delete this account"
                plain
                @click="deleteUser(row.item.id)"
              >
                <v-icon dark>delete</v-icon>
              </v-btn>
            </td>
          </tr>
        </template>
      </v-data-table>
      <h2>Devices</h2>
      <v-btn @click="addDevice" plain>Create new device</v-btn>
      <v-data-table :headers="deviceHeaders" :items="devices">
        <template v-slot:item="row">
          <tr @click="updateDevice(row.item)">
            <td>{{ row.item.name }}</td>
            <td>{{ row.item.address }}</td>
            <td>{{ row.item.description }}</td>
            <td>{{ row.item.maximumHourlyEnergyConsumption }}</td>
            <td>{{ row.item.ownerUsername }}</td>
            <td>
              <v-btn
                @click="deleteDevice(row.item.id)"
                title="Delete this device"
                plain
              >
                <v-icon dark>delete</v-icon>
              </v-btn>
            </td>
          </tr>
        </template>
      </v-data-table>
    </div>
    <div>
      <UserDialog
        :opened="userDialogOpened"
        :user="selectedUser"
        @refresh="refreshUserList"
      ></UserDialog>
      <DeviceDialog
        :opened="deviceDialogOpened"
        :device="selectedDevice"
        :usernames="usernames"
        @refresh="refreshDeviceList"
      ></DeviceDialog>
    </div>
    Select a user to chat with:
    <v-select :items="usernames" v-model="username"></v-select>
    <v-btn @click="openChat">Open chat</v-btn>
    <div class="chat" id="chat">
      <ul class="messages">
        <li v-for="(message, index) in messages" :key="index">
          <div v-if="isMessageInConversation(message)">
            <span class="username"
              ><b>{{ message.from }}: </b></span
            >
            <span class="message" v-if="isMessageInConversation(message)">{{
              message.text
            }}</span>
            &nbsp;&nbsp;&nbsp;&nbsp;<font-awesome-icon
              v-if="sameUserAndSeen(message)"
              icon="fas fa-check-double"
              id="seenIcon"
            />
          </div>
        </li>
        <i id="typing-notification" style="visibility: hidden"
          >{{ username }} is typing...</i
        >
      </ul>

      <v-textarea
        v-model="message"
        class="chat-t-f"
        label="Message"
        @input="typing"
        no-resize
      ></v-textarea>
      <v-btn plain class="send-button" @click="sendMessageToUser"
        ><font-awesome-icon class="chat-send" icon="paper-plane"
      /></v-btn>
      <v-btn plain class="close-button" @click="closeChat">X</v-btn>
    </div>
  </div>
</template>

<script>
import api from "@/api";
import UserDialog from "@/dialogs/UserDialog";
import DeviceDialog from "@/dialogs/DeviceDialog";

import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/* import paper plane icon */
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";

/* import double check icon */
import { faCheckDouble } from "@fortawesome/free-solid-svg-icons";

import SockJS from "sockjs-client";
import * as Stomp from "webstomp-client";

/* add icons to the library */
library.add(faPaperPlane, faCheckDouble);

// or .add(brands, solid) if you need the whole solid style icons library/module

export default {
  name: "AdminView",
  components: { DeviceDialog, UserDialog, FontAwesomeIcon },
  data() {
    return {
      users: [],
      devices: [],
      userHeaders: [
        { text: "Username", value: "username" },
        { text: "Role", value: "role" },
        { text: "Delete" },
      ],
      deviceHeaders: [
        { text: "Name", value: "name" },
        { text: "Address", value: "address" },
        { text: "Description", value: "description" },
        {
          text: "Maximum hourly energy consumption",
          value: "maximumHourlyEnergyConsumption",
        },
        { text: "Owner username", value: "ownerUsername" },
        { text: "Delete" },
      ],
      userDialogOpened: false,
      deviceDialogOpened: false,
      selectedUser: {},
      selectedDevice: {},
      usernames: [],
      message: "",
      messages: [],
      username: "",
      typingDelayMillis: 3000,
      lastTypedTime: new Date(0),
    };
  },
  methods: {
    addUser() {
      this.selectedUser = {};
      this.userDialogOpened = true;
    },
    updateUser(user) {
      this.selectedUser = user;
      this.userDialogOpened = true;
    },
    deleteUser(id) {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user.id === id) {
        alert("You can't delete yourself!");
        return;
      }
      api.users.deleteUser(id).then(() => {
        this.refreshUserList();
        this.refreshDeviceList();
      });
    },
    async refreshUserList() {
      this.userDialogOpened = false;
      this.users = await api.users.getUsers();
      this.usernames = this.users
        .filter((user) => user.role === "USER")
        .map((user) => user.username);
    },
    addDevice() {
      this.selectedDevice = {};
      this.deviceDialogOpened = true;
    },
    deleteDevice(id) {
      api.devices.deleteDevice(id).then(() => {
        this.refreshDeviceList();
      });
    },
    updateDevice(device) {
      this.selectedDevice = device;
      this.deviceDialogOpened = true;
    },
    async refreshDeviceList() {
      this.deviceDialogOpened = false;
      this.devices = await api.devices.getDevices();
    },
    takeUsername(user) {
      this.usernames.push(user.username);
    },
    openChat() {
      if (this.username === "") {
        alert("Please select a user to chat with!");
        return;
      }
      document.getElementById("chat").style.visibility = "visible";
      this.stompClient.send("/ws/user/mark-messages", this.username);
      this.refreshTypingStatus();
    },
    closeChat() {
      document.getElementById("chat").style.visibility = "hidden";
    },
    sendMessageToUser() {
      const user = JSON.parse(localStorage.getItem("user"));
      const messageToSend = {
        to: this.username,
        from: user.username,
        text: this.message,
      };
      this.stompClient.send("/ws/user", JSON.stringify(messageToSend));
      this.message = "";
      this.messages.push(messageToSend);
    },
    isMessageInConversation(message) {
      return message.from === this.username || message.to === this.username;
    },
    sameUserAndSeen(message) {
      const user = JSON.parse(localStorage.getItem("user"));
      return (
        message.from === user.username &&
        message.seen &&
        message.to === this.username
      );
    },
    typing() {
      this.lastTypedTime = new Date();
    },
    chatIsOpen() {
      return document.getElementById("chat").style.visibility === "visible";
    },
    refreshTypingStatus() {
      const status = this;
      setInterval(function () {
        console.log(this.username);
        if (
          new Date().getTime() - status.lastTypedTime.getTime() <
          status.typingDelayMillis
        ) {
          status.stompClient.send(
            "/ws/user/typing",
            JSON.stringify({
              toUsername: status.username,
              isTyping: true,
            })
          );
        } else {
          status.stompClient.send(
            "/ws/user/typing",
            JSON.stringify({
              toUsername: status.username,
              isTyping: false,
            })
          );
        }
      }, 100);
    },
  },
  async created() {
    this.users = await api.users.getUsers();
    this.usernames = this.users
      .filter((user) => user.role === "USER")
      .map((user) => user.username);
    this.devices = await api.devices.getDevices();
  },
  mounted: function () {
    const onMessageReceived = (payload) => {
      this.messages.push(JSON.parse(payload.body));
      if (document.getElementById("chat").style.visibility === "visible") {
        this.stompClient.send("/ws/user/mark-messages", this.username);
      }
    };

    const onSeenMessageReceived = (payload) => {
      const username = payload.body;
      this.messages.forEach((message) => {
        if (message.to === username) {
          message.seen = true;
        }
      });
      this.messages.push({
        from: "",
        to: "",
        text: "",
      });
      this.messages.pop();
    };

    const onTypingMessageReceived = (payload) => {
      const object = JSON.parse(payload.body);
      const isTyping = object.isTyping;
      const username = object.fromUsername;
      console.log("BRO " + this.chatIsOpen());
      if (username === this.username && isTyping && this.chatIsOpen()) {
        console.log("typing");
        document.getElementById("typing-notification").style.visibility =
          "visible";
      } else {
        console.log("not typing");
        document.getElementById("typing-notification").style.visibility =
          "hidden";
      }
    };

    const onConnected = () => {
      this.stompClient.subscribe("/topic/message/admin", onMessageReceived);
      this.stompClient.subscribe(
        "/topic/messages-seen/admin",
        onSeenMessageReceived
      );
      this.stompClient.subscribe(
        "/topic/typing/admin",
        onTypingMessageReceived
      );
    };

    function onError() {
      console.log("onError");
    }

    console.log("Connecting to websocket");
    this.socket = new SockJS("http://localhost:8081/ws");
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, onConnected, onError);
  },
};
</script>

<style scoped>
.chat {
  position: fixed;
  top: 45px;
  right: 0;
  margin: 20px;
  width: 500px;
  height: 500px;
  background-color: slategray;
  visibility: hidden;
}

.chat-t-f {
  position: absolute;
  bottom: 0;
  width: 100%;
  background-color: aliceblue;
}

.send-button {
  position: absolute;
  bottom: 0;
  right: 0;
  margin: 20px;
}

.close-button {
  position: absolute;
  top: 0;
  right: 0;
  margin: 20px;
}

.messages {
  list-style-type: none;
  margin-top: 10px;
  overflow-y: scroll;
  height: 310px;
}
</style>
