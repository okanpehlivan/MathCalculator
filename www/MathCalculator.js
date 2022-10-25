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

module.exports.rectangle = function (input, success, error) {
  exec(success, error, "MathCalculator", "rectangle", [input]);
};

module.exports.start = function (options, success, error) {
  exec(success, error, "MathCalculator", "start", [options]);
};
