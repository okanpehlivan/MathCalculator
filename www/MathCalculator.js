var exec = require("cordova/exec");

module.exports.add = function (arg0, success, error) {
  exec(success, error, "MathCalculator", "add", [arg0]);
};

module.exports.locationManager = function (arg0, success, error) {
  exec(success, error, "MathCalculator", "locationManager", [arg0]);
};

module.exports.hello = function (input, success, error) {
  exec(success, error, "MathCalculator", "hello", [input]);
};
