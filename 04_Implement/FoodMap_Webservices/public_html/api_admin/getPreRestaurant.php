<?php  
include "../../private/checkToken.php";

class Restaurant{
	function Restaurant($id, $owner_username, $name, $address, $phone_number, $describe_text, $url_image, $time_open, $time_close, $lat, $lon){
		$this->id = $id;
		$this->owner_username= $owner_username;
		$this->name = $name;
		$this->address = $address;
		$this->phone_number = $phone_number;
		$this->describe_text = $describe_text;
		$this->url_image = $url_image;
		$this->time_open = $time_open;
		$this->time_close = $time_close;
		$this->location["lat"] = $lat;
		$this->location["lon"] = $lon;
	}
}

$response = array();

if (isset($_POST["username"]) && isset($_POST["password"]))
{
	$username = $_POST["username"];
	$password = $_POST["password"];

	$conn = new database();
	$conn->connect();

	$check = $conn->LoginAdmin($username, $password);
	if ($check != -1)
	{
		$listRestaurant = $conn->GetRestaurantForAdmin();
		if ($listRestaurant != -1)
		{
			$data = array();
			foreach ($listRestaurant as $row) {
				array_push($data, new Restaurant($row["ID"], $row["OWNER_USERNAME"],$row["NAME"],$row["ADDRESS"],$row["PHONE_NUMBER"],$row["DESCRIBE_TEXT"],$row["URL_IMAGE"],$row["TIMEOPEN"],$row["TIMECLOSE"],$row["LAT"],$row["LON"]));
			}

			$response["status"] = 200;
			$response["message"] = "Success";
			$response["data"] = $data;
		}
		else
		{
			$response["status"] = 404;
			$response["message"] = "Exec Fail";
		}
	}
	else
	{
		$response["status"] = 444;
		$response["message"] = "Unauthorized";
	}

	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invaild Request";
}

echo json_encode($response);
?>