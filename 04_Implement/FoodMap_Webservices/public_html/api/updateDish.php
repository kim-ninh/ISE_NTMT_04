<?php

include "../../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["name"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$name = $_POST["name"];
	$token = $_POST["token"];
	$valueCol = "";

	foreach ($_POST as $key => $value) 
	{
		if ($key != "id_rest" && $key != "name" && $key != "token")
		{
			if ($key != "url_image")
			{
				$valueCol .= strtoupper($key) . "=" . $value .",";
			}
			else 
			{
				$valueCol .= strtoupper($key) . '="' . $value .'",';
			}
		}
	}
	//check token
	$check = checkTokenForRestaurant($id_rest, $token);

	if ($check == true)
	{
		$valueCol[strlen($valueCol) - 1] = ' ';
		if ($id_rest != '' && $name != '')
		{
			$conn = new database();
			$conn->connect();

			if ($conn->UpdateDish($id_rest, $name, $valueCol) != -1)
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
			$response["status"] = 404;
			$response["message"] = "Id or name not found";
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