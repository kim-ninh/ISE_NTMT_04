<?php 
//import library
include "../private/database.php";

if (isset($_POST["username"]) && isset($_POST["password"]))	
{
	class Owner{
		function Owner($username, $password, $name, $phone_number, $email, $url_image, $token){
			$this->username = $username;
			$this->password = $password;
			$this->name = $name;
			$this->phone_number = $phone_number;
			$this->email = $email;
			$this->url_image = $url_image;
			$this->token = $token;
		}
	}
	
	//create class Restaurant
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	//create connection
	$conn = new database();
	//connect
	$conn->connect();
	
	$response = array();

	$account = $conn->Login($username, $password);
	if ($account != -1)
	{
		$owner = '';
		foreach ($account as $row) {
			$token = $conn->GetToken($row["USERNAME"]);
			$owner = new Owner($row["USERNAME"], $row["PASSWORD"], $row["NAME"], $row["PHONE_NUMBER"], $row["EMAIL"], $row["URL_IMAGE"], $token);
			break;
		}
		
	    $response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $owner;
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Not Found";
	}
	//close conn
	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}

//response
echo json_encode($response);
?>