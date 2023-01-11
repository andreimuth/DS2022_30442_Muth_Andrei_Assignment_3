import authorizationHeader, { BASE_URL, HTTP } from "../http";

export default {
  getRoles() {
    return HTTP.get(BASE_URL + "/roles/find-all", {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
};
