global.addition = function(a, b) {
  return a + b;
};

global.renderServer = function(data) {
  var json = JSON.parse(data);
  return addition(json.a, json.b);
};