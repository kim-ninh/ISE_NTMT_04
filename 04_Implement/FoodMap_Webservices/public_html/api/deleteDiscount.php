<?php  
include "../../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["token"]) && isset($_POST["id_discount"]))
{
	$token = $_POST["token"];
	$id_rest = $_POST["id_rest"];
	$id_discount = $_POST["id_discount"];

	$check = checkTokenForRestaurant($id_rest, $token);
	if ($check)
	{
		$conn = new database();
		$conn->connect();

		if ($conn->DeleteDiscount($id_discount) != -1)
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