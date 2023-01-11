import authorizationHeader, { BASE_URL, HTTP } from "../http";

export default {
  getDevices() {
    return HTTP.get(BASE_URL + "/devices/find-all", {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  create(device) {
    return HTTP.post(BASE_URL + "/devices/create-device", device, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  update(id, device) {
    return HTTP.put(BASE_URL + "/devices/update-device/" + id, device, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  deleteDevice(id) {
    return HTTP.delete(BASE_URL + "/devices/delete-device/" + id, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  getDevicesByOwner(id) {
    return HTTP.get(BASE_URL + "/devices/find-by-owner/" + id, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  }
};
