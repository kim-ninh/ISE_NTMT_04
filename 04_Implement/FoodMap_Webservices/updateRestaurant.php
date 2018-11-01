<?php

include "../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["token"]))
{
	$id_rest = $_POST["id_rest"];
	$token = $_POST["token"];
	$valueCol = "";

	foreach ($_POST as $key => $value) 
	{
		if ($key != "id" && $key != "token")
		{
			$valueCol .= strtoupper($key) . '= "' . $value .'",';
		}
	}

	$check = checkToken($token);

	if ($check == true)
	{
		$valueCol[strlen($valueCol) - 1] = ' ';
		if ($id != '')
		{
			$conn = new database();
			$conn->connect();

			if ($conn->query($strQuery) != -1)
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