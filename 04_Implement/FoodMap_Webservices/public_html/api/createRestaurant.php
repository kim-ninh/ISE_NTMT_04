<?php
include "../../private/checkToken.php";

$response = array();

if (isset($_POST["owner_username"]) && isset($_POST["name"]) && isset($_POST["address"]) && isset($_POST["phone_number"]) && isset($_POST["describe_text"]) && isset($_POST["timeopen"]) && isset($_POST["timeclose"]) && isset($_POST["lat"]) && isset($_POST["lon"]) && isset($_POST["token"]))
{
	$valueRes = '';
	
	$lat =  $_POST["lat"];
	$lon =  $_POST["lon"];
	$token = $_POST["token"];

	// check token
	$check = checkToken($token);

	if ($check == true)
	{
		foreach ($_POST as $key => $value) 
		{
			if ($key != 'lat' && $key != 'lon' && $key != 'token')
			{
				$valueRes .= strtoupper($key) . ' = "'. $value . '",';
			}
		}
		$valueRes[strlen($valueRes) - 1] = ' '; // xóa dấu ',' cuối


		$conn = new database();
		$conn->connect();
		$id = $conn->CreateRestaurant($valueRes, $lat, $lon);
		if ($id !== -1)
		{
			$response["status"] = 200;
			$response["message"] = "Success";
			$response["id"] = $id;
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