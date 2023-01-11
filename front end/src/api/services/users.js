import authorizationHeader, { BASE_URL, HTTP } from "../http";

export default {
  getUsers() {
    return HTTP.get(BASE_URL + "/users/find-all", {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  create(user) {
    return HTTP.post(BASE_URL + "/users/create-user", user, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  update(id, user) {
    return HTTP.put(BASE_URL + "/users/update-user/" + id, user, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  deleteUser(id) {
    return HTTP.delete(BASE_URL + "/users/delete-user/" + id, {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
  findNonAdminUsers() {
    return HTTP.get(BASE_URL + "/users/find-non-admins", {
      headers: authorizationHeader(),
    }).then((response) => {
      return response.data;
    });
  },
};
