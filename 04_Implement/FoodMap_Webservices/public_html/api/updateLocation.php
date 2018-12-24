<?php

include "../../private/checkToken.php";

if (isset($_POST["id_rest"]) && isset($_POST["lat"]) && isset($_POST["lon"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$lat = $_POST["lat"];
	$lon = $_POST["lon"];
	$token = $_POST["token"];

	$check = checkTokenForRestaurant($id_rest, $token);

	if ($check == true)
	{
		$conn = new database();
		$conn->connect();
		if ($conn->UpdateLocation($id_rest, $lat, $lon) != -1)
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