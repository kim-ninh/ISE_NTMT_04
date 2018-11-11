<?php
include "../private/database.php";

$response = array();

if (isset($_POST["name"]) && isset($_POST["email"]))
{
	$name = $_POST["name"];
	$email = $_POST["email"];


	$conn = new database();
	$conn->connect();
	if ($conn->AddGuest($email, $name) != -1)
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