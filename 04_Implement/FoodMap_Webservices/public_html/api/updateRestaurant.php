<?php

include "../../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$token = $_POST["token"];
	$valueCol = "";
	$lat = $_POST["lat"];
	$lon = $_POST["lon"];

	foreach ($_POST as $key => $value) 
	{
		if ($key != "id_rest" && $key != "token" && $key != "lat" && $key != "lon")
		{
			$valueCol .= strtoupper($key) . ' = "' . $value .'",';
		}
	}

	$check = checkTokenForRestaurant($id_rest, $token);

	if ($check == true)
	{
		$valueCol[strlen($valueCol) - 1] = ' ';
		if ($id_rest != '')
		{
			$conn = new database();
			$conn->connect();

			if ($conn->UpdateRestaurant($id_rest, $valueCol) != -1)
			{
				if ($lat != -1 && $lon != -1)
				{
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
				}
				else
				{
					$response["status"] = 404;
					$response["message"] = "Exec fail";
				}
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
			$response["status"] = 404;
			$response["message"] = "Id not found";
		}
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