<?php
include "../private/mail.php";
include "../private/database.php";

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

		if ($check == true)
		{
			$response["status"] = 200;
			$response["message"] = "Success";
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

			$body = "<h1>Mã reset password của bạn là:</h1><br>".$code."<b><b/>";

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