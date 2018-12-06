<?php
$respone = array();

if (isset($_POST["url"]))
{
	if (file_exists("../images/".$id)) {
        $url = $_POST["url"];
		unlink($url);

		$respone["status"] = 200;
		$respone["message"] = "Success";
    }
    else
    {
    	$respone["status"] = 404;
		$respone["message"] = "Not Found";
    }
}
else
{
	$respone["status"] = 400;
	$respone["message"] = "Invaild request";
}

echo json_encode($respone);
?>