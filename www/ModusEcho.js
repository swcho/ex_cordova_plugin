var argscheck = require('cordova/argscheck');
var utils = require('cordova/utils');
var exec = require('cordova/exec');
var cordova = require('cordova');

exports.echo = function (arg0, success, error) {
    exec(success, error, "ModusEcho", "echo", [arg0]);
};

exports.echojs = function (arg0, success, error) {
    if (arg0 && typeof (arg0) === 'string' && arg0.length > 0) {
        success(arg0);
    } else {
        error('Empty message!');
    }
};

exports.ntp = function(success, error) {
    exec(success, error, "ModusEcho", "ntp");
}