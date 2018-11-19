<?php  
include "../private/database.php";

$response = array();

if (isset($_POST["id_offer"]))
{
	$conn = new database();
	$conn->connect();
	if ($conn->DeteleOffer($_POST["id_offer"]) != -1)
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
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}

echo json_encode($response);
?>