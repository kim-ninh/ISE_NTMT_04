<?php  
include '../private/database.php';

$response = array();

if (isset($_POST["guest_email"]) && isset($_POST["total"]) && isset($_POST["id_discount"]) )
{
	$conn = new database();
	$conn->connect();
	
	$check = $conn->addOffer($_POST["guest_email"], $_POST["total"], $_POST["id_discount"]);

	if ($check != -1)
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