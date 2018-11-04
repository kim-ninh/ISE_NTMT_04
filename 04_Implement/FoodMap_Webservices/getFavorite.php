<?php  
include '../private/database.php';

class Favorite{
	Favorite($id_rest, $guest_email){
		$this->id_rest = $id_rest;
		$this->guest_email = $guest_email;
	}
}

$response = array();

if (isset($_GET["id_rest"]))
{
	$conn = new database();
	$conn->connect();
	$listFavorite = $conn->GetFavorite($_GET["id_rest"]);

	if ($listFavorite != -1)
	{
		$favorites = array();
		foreach ($listFavorite as $row) {
			array_push($favorites, new Favorite($row["ID_REST"], $row["GUEST_EMAIL"]));
		}
		
		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $favorites;
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