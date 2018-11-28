<?php  
include '../../private/checkToken.php';

$response = array();

if (isset($_POST["token"]) && isset($_POST["id_rest"]) && isset($_POST["namedish"]) && isset($_POST["discount_percent"]) && isset($_POST["timestart"] && isset($_POST["timeend"]))
{
	$check = checkTokenForRestaurant($_POST["id_rest"], $_POST["token"]);

	if ($check == true)
	{
		$conn = new database();
		$conn->connect();
		

		$check = $conn->CreateDiscount($_POST["id_rest"], $_POST["namedish"], $_POST["discount_percent"], $_POST["timestart"], $_POST["timeend"]);

		if ($check != -1 && $check != false)
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
		$response["message"] = "Invalid Token";
	}
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}

echo json_encode($response);
?>