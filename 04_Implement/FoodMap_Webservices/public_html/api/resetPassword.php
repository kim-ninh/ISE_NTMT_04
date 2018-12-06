<?php
include "../../private/mail.php";
include "../../private/database.php";
class Owner{
		function Owner($username, $password, $name, $phone_number, $email, $url_image, $token){
			$this->username = $username;
			$this->password = $password;
			$this->name = $name;
			$this->phone_number = $phone_number;
			$this->email = $email;
			$this->url_image = $url_image;
			$this->token = $token;
		}
	}

$response = array();

if (isset($_POST["email"]))
{
	$email = $_POST["email"];
	$mail = new Mail();
	$mail->SetTo($email);
	$body = "";
	$subject = "";

	$conn = new database();
	$conn->connect();
	 // kiểm tra mã reset
	if (isset($_POST["code"]))
	{
		$code = $_POST["code"];
		$check = $conn->CheckCode($email, $code);

		if ($check != -1)
		{
			$response["status"] = 200;
			$response["message"] = "Success";

			foreach ($check as $row) 
			{
				$token = $conn->GetToken($row["USERNAME"]);
				$owner = new Owner($row["USERNAME"], $row["PASSWORD"], $row["NAME"], $row["PHONE_NUMBER"], $row["EMAIL"],$row["URL_IMAGE"], $token);
				$response["data"] = $owner;
				break;
			}
		}
		else
		{
			$response["status"] = 404;
			$response["message"] = "Code not found";
		}
	}
	else // tạo mã reset và gửi code đến mail
	{
		$code = $conn->GetCode($email);

		if ($code != -1) // get code thành công
		{
			$response["status"] = 200;
			$response["message"] = "Success";

			$body = "<h1>Mã reset password của bạn là:</h1><b>".$code."<b/>";
			$subject = "Đặt lại mật khẩu";
			$mail->SetSubject($subject);
			$mail->SetContext($body);
			$mail->SendMail();
		}
		else // email ko tồn tại
		{
			$response["status"] = 404;
			$response["message"] = "Email not found";
		}
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