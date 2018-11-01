<?php
require_once('PHPMailer/src/PHPMailer.php');
require_once('PHPMailer/src/Exception.php');
require_once('PHPMailer/src/SMTP.php');

class Mail
{
	private $mail;

	function Mail()
	{
		$this->mail= new PHPMailer\PHPMailer\PHPMailer();
		$this->mail->CharSet = 'UTF-8';
		$this->mail->IsSMTP();
		$this->mail->Host       = 'smtp.gmail.com';

		$this->mail->SMTPSecure = 'tls';
		$this->mail->Port       = 587;
		$this->mail->SMTPDebug  = 2;
		$this->mail->SMTPAuth   = true;
		$this->mail->SMTPDebug = 0;

		$this->mail->Username   = 'the.dreamers.k16';
		$this->mail->Password   = 'iNMAZso1A22j';

		$this->mail->SetFrom('the.dreamers.k16@gmail.com', 'FoodMap Support');
		$this->mail->AddReplyTo('no-reply@foodmap.tk','no-reply');
	}
	
	public function SendMail()
	{
		$this->mail->send();
	}	

	public function SetTo($to)
	{
		$this->mail->AddAddress($to, 'Owner');
	}

	public function SetSubject($subject)
	{
		$this->mail->Subject = $subject;
	}

	public function SetContext($context)
	{
		$this->mail->MsgHTML($context);
	}

}
?>