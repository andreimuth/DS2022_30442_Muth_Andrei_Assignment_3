module.exports = {
  transpileDependencies: ["vuetify"],
};

//const fs = require("fs");

module.exports = {
  devServer: {
    port: 8080,
    //https: true,
    //key: fs.readFileSync("C:/Montran Internship/frontend/src/key.pem"),
    //cert: fs.readFileSync("C:/Montran Internship/frontend/src/cert.pem"),
    //hotOnly: false,
    public: "http://localhost:8080/",
  },
};
