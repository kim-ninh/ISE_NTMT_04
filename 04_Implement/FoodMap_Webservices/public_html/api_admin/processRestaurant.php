<?php  
include "../../private/checkToken.php";
include "../../private/mail.php";

$response = array();

if (isset($_POST["username"]) && isset($_POST["password"]) && isset($_POST["id_rest"]) && isset($_POST["status"]))
{
	$username = $_POST["username"];
	$password = $_POST["password"];
	$id_rest = $_POST["id_rest"];
	$status = $_POST["status"];

	$conn = new database();
	$conn->connect();

	$check = $conn->LoginAdmin($username, $password);
	if ($check != -1)
	{
		$result = -1;
		$note = "";

		if ($status == 0)
		{
			$result = $conn->DeletePreRestaurantAdmin($id_rest);
		}
		else if ($status == 1) 
		{
			if (isset($_POST["note"]))
			{
				$note = $_POST["note"];
			}
			$result = $conn->UpdatePreRestaurant($id_rest, 1);
		}
		

		if ($result != -1)
		{
			$email = $conn->GetEmailOwner($id_rest);
			if ($email != -1)
			{
				$mail = new Mail();
				$mail->SetTo($email);
				$mail->SetSubject("Kiểm duyệt nhà hàng");
				if ($note != "")
				{
					$mail->SetContext($note);
				}
				else 
				{
					$mail->SetContext("Nhà hàng của bạn đã được kiểm duyệt");
				}
				$mail->SendMail();
			}

			$response["status"] = 200;
			$response["message"] = "Success";
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