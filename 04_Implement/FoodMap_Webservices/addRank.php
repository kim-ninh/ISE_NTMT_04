<?php  
include '../private/database.php';

$response = array();

if (isset($_POST["email_guest"]) && isset($_POST["id_rest"]) && isset($_POST["star"]) )
{
	$conn = new database();
	$conn->connect();
	
	$check = $conn->AddRank($_POST["id_rest"], $_POST["email_guest"], $_POST["star"]);

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