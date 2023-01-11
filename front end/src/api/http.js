import axios from "axios";

export const BASE_URL = "http://localhost:8081";
export const HTTP = axios.create({
  baseURL: BASE_URL,
});

export default function authorizationHeader() {
  let user = JSON.parse(localStorage.getItem("user"));
  if (user && user.token) {
    return {
      Authorization: "Bearer " + user.token,
    };
  } else {
    return {};
  }
}
