var express = require('express');
var app = express();

app.get('/nodejs-test', function(req, res){
  res.send('Hello World test');
});

var server = app.listen(3000, function() {
    console.log('Listening on port %d', server.address().port);
});
