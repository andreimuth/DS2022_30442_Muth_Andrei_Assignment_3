<template>
  <div>
    <v-btn @click="logout" plain><v-icon>mdi-arrow-left</v-icon>Log out</v-btn>
    <h1>Welcome, {{ getUsername }}!</h1>
    <h2>Your devices</h2>
    <v-data-table
      :headers="deviceHeaders"
      :items="devices"
      @click:row="openDateDialog"
    ></v-data-table>
    <DateAndChartDialog
      :opened="dateDialogOpened"
      :calendarMode="calendarMode"
      :device="selectedDevice"
      @closeDialog="closeDateDialog"
    ></DateAndChartDialog>
    <v-btn class="open-chat" plain @click="openChat">Talk to admin</v-btn>
    <div class="chat" id="chat">
      <ul class="messages">
        <li v-for="(message, index) in messages" :key="index">
          <span class="username"
            ><b>{{ message.from }}: </b></span
          >
          <span class="message">{{ message.text }}</span>
          &nbsp;&nbsp;&nbsp;&nbsp;<font-awesome-icon
            v-if="isSameUserAndSeen(message)"
            icon="fas fa-check-double"
            id="seenIcon"
          />
        </li>
        <i id="typing-notification" style="visibility: hidden"
          >admin is typing...</i
        >
      </ul>
      <v-textarea
        v-model="message"
        class="chat-t-f"
        label="Message"
        no-resize
        @input="typing"
      ></v-textarea>
      <v-btn plain class="send-button" @click="sendMessageToAdmin"
        ><font-awesome-icon class="chat-send" icon="paper-plane"
      /></v-btn>
      <v-btn plain class="close-button" @click="closeChat">X</v-btn>
    </div>
  </div>
</template>

<script>
import api from "@/api";
import DateAndChartDialog from "@/dialogs/DateAndChartDialog";

import * as Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/* import paper plane icon */
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";

import { faCheckDouble } from "@fortawesome/free-solid-svg-icons";

/* add icons to the library */
library.add(faPaperPlane, faCheckDouble);

// or .add(brands, solid) if you need the whole solid style icons library/module

export default {
  name: "UserView",
  components: { DateAndChartDialog, FontAwesomeIcon },
  data() {
    return {
      devices: [],
      deviceHeaders: [
        { text: "Name", value: "name" },
        { text: "Address", value: "address" },
        { text: "Description", value: "description" },
        {
          text: "Maximum hourly energy consumption",
          value: "maximumHourlyEnergyConsumption",
        },
        { text: "Owner", value: "ownerUsername" },
      ],
      dateDialogOpened: false,
      calendarMode: false,
      selectedDevice: {},
      message: "",
      messages: [],
      typingDelayMillis: 3000,
      lastTypedTime: new Date(0),
    };
  },
  methods: {
    openDateDialog(device) {
      this.selectedDevice = device;
      this.calendarMode = true;
      this.dateDialogOpened = true;
    },
    closeDateDialog() {
      this.selectedDevice = {};
      this.dateDialogOpened = false;
    },
    logout() {
      this.unsubscribe();
      this.$store.dispatch("auth/logout");
      this.$router.push("/");
    },
    unsubscribe() {
      console.log("Unsubscribing...");
      const user = JSON.parse(localStorage.getItem("user"));
      this.stompClient.unsubscribe("/topic/notify/user/" + user.id);
      this.stompClient.disconnect();
    },
    openChat() {
      console.log("Connected to chat");
      const user = JSON.parse(localStorage.getItem("user"));

      document.getElementById("chat").style.visibility = "visible";
      this.stompClient.send("/ws/admin/mark-messages", user.username);
    },
    closeChat() {
      document.getElementById("chat").style.visibility = "hidden";
    },
    onChatMessageReceived(payload) {
      const message = JSON.parse(payload.body);
      this.messages.push(message);
      console.log(this.messages.length);
      if (document.getElementById("chat").style.visibility === "visible") {
        this.stompClient.send("/ws/admin/mark-messages", message.to);
      }
    },
    sendMessageToAdmin() {
      const user = JSON.parse(localStorage.getItem("user"));
      const messageToSend = {
        from: user.username,
        text: this.message,
      };
      this.stompClient.send("/ws/admin", JSON.stringify(messageToSend));
      this.message = "";
      this.messages.push(messageToSend);
    },
    isSameUserAndSeen(message) {
      const user = JSON.parse(localStorage.getItem("user"));
      return message.from === user.username && message.seen;
    },
    onSeenMessagesReceived() {
      this.messages.forEach((message) => {
        message.seen = true;
      });
      this.messages.push({});
      this.messages.pop();
    },
    chatIsOpen() {
      return document.getElementById("chat").style.visibility === "visible";
    },
    onTypingMessageReceived(payload) {
      const object = JSON.parse(payload.body);
      const isTyping = object.isTyping;
      console.log("sdada "  + this.chatIsOpen());
      if(isTyping && this.chatIsOpen()) {
        document.getElementById("typing-notification").style.visibility = "visible";
      } else {
        document.getElementById("typing-notification").style.visibility = "hidden";
      }
    },
    typing() {
      this.lastTypedTime = new Date();
    },
    refreshTypingStatus() {
      const status = this;
      setInterval(function () {
        if (
          new Date().getTime() - status.lastTypedTime.getTime() <
          status.typingDelayMillis
        ) {
          status.stompClient.send(
            "/ws/admin/typing",
            JSON.stringify({
              fromUsername: JSON.parse(localStorage.getItem("user")).username,
              isTyping: true,
            })
          );
        } else {
          status.stompClient.send(
            "/ws/admin/typing",
            JSON.stringify({
              fromUsername: JSON.parse(localStorage.getItem("user")).username,
              isTyping: false,
            })
          );
        }
      }, 100);
    },
  },
  computed: {
    getUsername: function () {
      const data = JSON.parse(localStorage.getItem("user"));
      return data.username;
    },
  },
  async created() {
    const user = JSON.parse(localStorage.getItem("user"));
    this.devices = await api.devices.getDevicesByOwner(user.id);
  },
  mounted() {
    const onMessageReceived = (payload) => {
      alert(payload.body);
    };

    const onConnected = () => {
      console.log("Connection to websocket established");
      const user = JSON.parse(localStorage.getItem("user"));
      this.stompClient.subscribe(
        "/topic/notify/user/" + user.id,
        onMessageReceived
      );
      this.stompClient.subscribe(
        "/topic/message/user/" + user.id,
        this.onChatMessageReceived
      );
      this.stompClient.subscribe(
        "/topic/messages-seen/user/" + user.id,
        this.onSeenMessagesReceived
      );
      this.stompClient.subscribe(
        "/topic/typing/user/" + user.id,
        this.onTypingMessageReceived
      );
    };

    function onError() {
      console.log("onError");
    }

    console.log("Connecting to websocket");
    this.socket = new SockJS("http://localhost:8081/ws");
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect({}, onConnected, onError);
    this.refreshTypingStatus();
  },
};
</script>

<style scoped>
.open-chat {
  position: fixed;
  top: 45px;
  right: 0;
  margin: 20px;
}

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
