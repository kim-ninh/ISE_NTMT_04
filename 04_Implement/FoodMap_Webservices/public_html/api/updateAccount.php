<?php

include "../../private/checkToken.php";

$response = array();

//requirement
if (isset($_POST["username"]) && isset($_POST["token"]))
{
	$username = "";
	$valueCol = "";
	
	$token = $_POST["token"];
	$username = $_POST["username"];

	//create update string
	foreach ($_POST as $key => $value) 
	{
		if ($key != "username" && $key != "token")
		{
			//get value
			$valueCol .= strtoupper($key) . '= "' . $value .'",';
		}
	}
	$valueCol[strlen($valueCol) - 1] = ' ';

	$check = checkTokenForUsername($username, $token);

	if ($check == true)
	{
		//create query string
		$conn = new database();
		$conn->connect();

		if ($conn->UpdateAccount($username, $valueCol) != -1)
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
		$response["status"] = 444;
		$response["message"] = "Token Invalid";
	}
	
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}

echo json_encode($response);

?>