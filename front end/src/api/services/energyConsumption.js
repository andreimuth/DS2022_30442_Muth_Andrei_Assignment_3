import authorizationHeader, { BASE_URL, HTTP } from "../http";

export default {
  getEnergyConsumptions(id, day) {
    return HTTP.get(
      BASE_URL +
        "/energy-consumption/get-consumption-for-device/" +
        id +
        "/" +
        day,
      {
        headers: authorizationHeader(),
      }
    ).then((response) => {
      return response.data;
    });
  },
};
