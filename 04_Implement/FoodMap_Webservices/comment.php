<?php
include "../private/checkToken.php";

$response = array();

if (isset($_POST["id_rest"]) && isset($_POST["comment"]) && (isset($_POST["guest_email"]) || (isset($_POST["owner_email"]) && isset($_POST["token"]))))
{
	$strQuery = "";

	$date_time = new DateTime();
	$date_time = $date_time->format('Y-m-d H:i:s');
	$id_rest = $_POST["id_rest"];
	$comment = $_POST["comment"];
	
	$isOwner = false;
	$email = "";
	$check = false;

	if (isset($_POST["guest_email"]))
	{
		$email = $_POST["guest_email"];
	}
	else if (isset($_POST["owner_email"]))
	{
		$email = $_POST["owner_email"];
		$token = $_POST["token"];
		// kiểm tra token
		$check = checkToken($token);
		$isOwner = true;
	}

	$conn = new database();
	$conn->connect();

	if ($isOwner)
	{
		if (!$check)
		{
			$response["status"] = 444;
			$response["message"] = "Token Invalid";
		}
		else
		{
			//add dữ liệu
			if ($conn->AddComment($date_time, $id_rest, $email, $comment, $isOwner) != -1)
			{
				$response["status"] = 200;
				$response["message"] = "Success";
			}
			else
			{
				$response["status"] = 404;
				$response["message"] = "Exec fail";
			}
		}
	}
	else 
	{
		//add dữ liệu
		if ($conn->AddComment($date_time, $id_rest, $email, $comment, $isOwner) != -1)
		{
			$response["status"] = 200;
			$response["message"] = "Success";
		}
		else
		{
			$response["status"] = 404;
			$response["message"] = "Exec fail";
		}
	}
	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}

//lose echo json_decode
echo json_encode($response);
?>