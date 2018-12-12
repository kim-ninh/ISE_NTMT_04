<?php  
include "../../private/checkToken.php";

$response = array();

if (isset($_POST["token"]) && isset($_POST["id_offer"]) && isset($_POST["id_discount"]) && isset($_POST["status"]))
{
	$token = $_POST["token"];
	$id_offer = $_POST["id_offer"];
	$id_discount = $_POST["id_discount"];
	$status = $_POST["status"];

	$check = checkTokenForDiscount($id_discount, $token);
	if ($check)
	{
		$conn = new database();
		$conn->connect();

		if ($conn->UpdateStatusOffer($id_discount, $id_offer, $status) != -1)
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