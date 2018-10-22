<?php 
//import library
include "../private/database.php";
		
$response = array();

if (isset($_POST['id_rest']))	
{
	$id_rest = $_POST['id_rest'];

	//create class Comment
	class Location{
		function Location($id_rest, $lat, $lon){
			$this->id_rest = $id_rest;
			$this->lat = $lat;
			$this->lon = $lon;
		}
	}
		
	//create connection
	$conn = new database();
	//connect
	$conn->connect();

	//get result
	$listLocations = $conn->GetLocation($id_rest);

	if ($listLocations != -1)
	{
		foreach ($listLocations as $row) {
			$res =  new Location($row['ID_REST'], $row['LAT'], $row['LON']);

			$response["status"] = 200;
			$response["message"] = "Success";
			$response["data"] = $res;
			break; 
		}
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Exec fail";
	}

	//close conn
	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}
//response
echo json_encode($response);
?>