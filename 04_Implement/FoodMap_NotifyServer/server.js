var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");

var users = [];

server.on('listening',function(){
    console.log('ok, server is running');
});

server.listen(process.env.PORT || 3000);
  

io.sockets.on('connection', function (socket) {
	//io.sockets.emit('serverguitinnhan', { noidung: "okbaby" });
  socket.on('register', function (email) {//email of user ---------- send from user when connect
	if(email){
		index = users.findIndex(soc => soc.email === email);
			if (index == -1){
				socket.email = email;
				users.push(socket);
				console.log("Email: " + socket.email);
				console.log("Device ID: " + socket.id);
			}else{
				socket.email = email;
				users[index] = socket;
				console.log("Email: " + socket.email);
				console.log("Device ID: " + socket.id);
			}
	}
  });
  
  //structure of order
  
  //{
//		email_owner
//		email_guest
//		name_rest
//		id_rest
//		id_discount
//		order: {
//					namedish,
//					discount_percent,
//					guest_email,
//					total
//				}
  //}
  
  socket.on('send_order', function(data){ //
	  console.log('send_order' + data);
	  var order = JSON.parse(data);
	  var index = users.findIndex(soc => soc.email === order.email_owner);
	  if(index != -1){
		  var id = users[index].id;
		  console.log('id_send_order' + id);
		  io.to(id).emit('receive_order', data);
	  }else{
		  socket.emit('receive_result', {status:404, message:'Chủ nhà hàng hiện đang offline!'});
	  }
  });
  
  
   //structure of result
  
  //{
//		email_guest
//		status
//		message
  //}
  
  socket.on('send_result', function(response){ //
	  console.log('send_result' + response);
	  var resp = JSON.parse(response);
	  var index = users.findIndex(soc => soc.email === resp.email_guest);
	  if(index != -1){
		  var id = users[index].id;
		  console.log('id_send_result' + id);
		  io.to(id).emit('receive_result', response);
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