<template>
  <v-dialog
    transition="dialog-bottom-transition"
    :max-width="width"
    :value="opened"
    persistent
    id="mainDialog"
  >
    <template>
      <v-card>
        <v-form v-if="calendarMode">
          <v-date-picker v-model="day"></v-date-picker>
        </v-form>
        <LineChart v-else :chartData="chartData" />
        <v-btn v-if="calendarMode" @click="seeChart">
          See en.<br />cons.chart
        </v-btn>
        <v-btn @click="closeDialog">Close dialog</v-btn>
      </v-card>
    </template>
  </v-dialog>
</template>

<script>
import LineChart from "@/components/LineChart";
import api from "@/api";
export default {
  name: "DateAndChartDialog",
  components: { LineChart },
  props: {
    opened: Boolean,
    device: Object,
  },
  data() {
    return {
      width: 290,
      day: null,
      calendarMode: true,
      chartData: {
        labels: [
          "0",
          "1",
          "2",
          "3",
          "4",
          "5",
          "6",
          "7",
          "8",
          "9",
          "10",
          "11",
          "12",
          "13",
          "14",
          "15",
          "16",
          "17",
          "18",
          "19",
          "20",
          "21",
          "22",
          "23",
        ],
        datasets: [
          {
            data: [],
            backgroundColor: ["#FF6384"],
            label: "Energy Consumption per Hour",
            borderColor: "rgb(75, 192, 200)",
          },
        ],
      },
    };
  },
  methods: {
    change() {
      console.log(this.day);
      console.log(this.device.id);
    },
    closeDialog() {
      this.width = 290;
      this.calendarMode = true;
      this.$emit("closeDialog");
    },
    async seeChart() {
      if (this.day === null) {
        alert("Please select a date");
      } else {
        this.chartData.datasets[0].data =
          await api.energyConsumption.getEnergyConsumptions(
            this.device.id,
            this.day
          );
        this.chartData.datasets[0].label =
          "Energy Consumption per Hour on " +
          this.day +
          " for device " +
          this.device.name;
        this.width = 600;
        this.calendarMode = false;
      }
    },
  },
};
</script>
