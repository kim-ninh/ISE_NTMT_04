<?php
$respone = array();

if (isset($_POST["url"]))
{
	$url = $_POST["url"];
	unlink($url);

	$respone["status"] = 200;
	$respone["message"] = "Success";
}
else
{
	$respone["status"] = 400;
	$respone["message"] = "Invaild request";
}

echo json_encode($respone);

?>