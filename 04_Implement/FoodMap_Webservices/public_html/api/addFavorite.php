<?php
include "../../private/database.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["guest_email"]))
{
	$id_rest = $_POST["id_rest"];
	$guest_email = $_POST["guest_email"];


	$conn = new database();
	$conn->connect();
	if ($conn->AddFavorite($id_rest, $guest_email) != -1)
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
	$response["message"] = "Invailed request";
}
echo json_encode($response);
?>