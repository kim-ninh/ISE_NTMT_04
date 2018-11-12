<?php  
include '../private/database.php';

$response = array();

if (isset($_POST["guest_email"]) && isset($_POST["id_rest"]) && isset($_POST["star"]) )
{
	$conn = new database();
	$conn->connect();

	if ($conn->AddRank($_POST["id_rest"], $_POST["guest_email"], $_POST["star"]) != -1)
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