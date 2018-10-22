<?php
include "../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["name"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$name = $_POST["name"];

	$token = $_POST["token"];
	
	$check = checkToken($token);

	if ($check == true)
	{
		$conn = new database();
		$conn->connect();
		
		if ($conn->DeleteDish($id_rest, $name) != -1)
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