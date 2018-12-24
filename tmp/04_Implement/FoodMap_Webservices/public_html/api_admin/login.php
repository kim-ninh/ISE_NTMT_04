<?php  
include "../../private/database.php";

$response = array();

if (isset($_POST["username"]) && isset($_POST["password"]))
{
	$username = $_POST["username"];
	$password = $_POST["password"];

	$conn = new database();
	$conn->connect();

	if ($conn->LoginAdmin($username, $password) != -1)
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
	$response["message"] = "Invalid Token";
}

echo json_encode($response);
?>