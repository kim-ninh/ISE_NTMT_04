<?php
include "../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$token = $_POST["token"];
	
	$check = checkToken($token);

	if ($check == true)
	{
		$conn = new database();
		$conn->connect();

		if ($conn->DeleteRestaurant($id_rest) != -1)
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