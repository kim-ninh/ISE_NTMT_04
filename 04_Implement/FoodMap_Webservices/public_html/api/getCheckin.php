<?php 
include "../../private/database.php"

$response = array();

if (isset($_POST["id_rest"]))
{
	$id_rest = $_POST["id_rest"];

	$conn = new database();
	$conn->connect();

	$listCheckin = $conn->GetCheckin($id_rest);

	if ($listCheckin != -1)
	{
		$data = 0;

		foreach ($listCheckin as $row) {
			$data = $row["COUNT"];
			break;
		}

		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $data;
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