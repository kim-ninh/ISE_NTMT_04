<?php
include "../private/database.php";

$response = array();

if (isset($_POST["username"]) && isset($_POST["password"]) && isset($_POST["name"]) && isset($_POST["phone_number"]) && isset($_POST["email"]))
{
	$username = $_POST["username"];
	$password = $_POST["password"];
	$name = $_POST["name"];
	$phone_number = $_POST["phone_number"];
	$email = $_POST["email"];


	$conn = new database();
	$conn->connect();
	if ($conn->CreateAccount($username, $password, $name, $phone_number, $email) != -1)
	{
		$response["status"] = 200;
		$response["message"] = "Success";
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Exec fail";
	}

	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invailed request";
}
echo json_encode($response);
?>