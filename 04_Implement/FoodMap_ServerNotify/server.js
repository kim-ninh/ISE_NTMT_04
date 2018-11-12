var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
server.listen(process.env.PORT || 3000);

var users = [];

io.sockets.on('connection', function (socket) {
	//io.sockets.emit('serverguitinnhan', { noidung: "okbaby" });
  
  socket.on('register', function (email) {//email of user ---------- send from user when connect
	if(email){
			socket.email = email;
			users.push(socket);
			console.log("Email: " + socket.email);
			console.log("ID: " + socket.id);
	}
  });
  
  //structure of order
  
  //{
//		email_owner
//		email_guest
//		id_rest
//		order: [ 
//					{id_dish, number}
//				]
  //}
  
  socket.on('send_order', function(data){ //
	  var order = JSON.parse(data);
	  var index = users.findIndex(soc => soc.email === order.email_owner);
	  if(index != -1){
		  var id = users[index].id;
		  io.to(id).emit('receive_order', data);
	  }else{
		  socket.emit('result', {status:404, message:'Order fail!'});
	  }
  });
  
  
   //structure of result
  
  //{
//		email_guest
//		status
//		message
  //}
  
  socket.on('send_result', function(response){ //
		console.log(response);
	  var resp = JSON.parse(response);
	  var index = users.findIndex(soc => soc.email === resp.email_guest);
	  if(index != -1){
		  var id = users[index].id;
		  io.to(id).emit('receive_result', response);
	  }else{
		  socket.emit('result', {status:404, message:'Turn back fail!'});
	  }
  });
  
  
  socket.on('disconnect', function(){
		var index = users.findIndex(soc => soc.id === socket.id);
		console.log("index: " + index);
		if (index != -1) 
			users.splice(index, 1);
        socket.disconnect(true);
        console.info('disconnected user (id=' + socket.id + ').');
    });
  
  
});