<?php 
	//import library
	include "../private/database.php";
	
	class Account{
		public $username;
		public $password;
		public $name;
		public $phone_number;
		public $email;
	}
	
	//create class Restaurant
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	//create query string
	$query = 'CALL SP_LOGIN("'.$username.'" , "'.$password.'")';
	
	//create connection
	$conn = new database();
	//connect
	$conn->connect();
	//get result
	$account = $conn->query($query);
	$response= array();

	if ($account != false && $account != null)
	{
		$res= new Account;
		foreach ($account as $row) {
			$res->username 	= $row['USERNAME'];
			$res->password 	= $row['PASSWORD'];
			$res->name 		= $row['NAME'];
			$res->phone_number = $row['PHONE_NUMBER'];
			$res->email		= $row['EMAIL'];
		}

		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $res;
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Not Found";
	}
	
	//close conn
	$conn->disconnect();
	//response
	echo json_encode($response);
?>