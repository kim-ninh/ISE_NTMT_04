<?php

$reponse = array();

if (isset($_POST["name"]) && isset($_POST["data"]) && isset($_POST["id"]))
{
	$img_name = $_POST["name"];
	$id = $_POST["id"];

	$data = $_POST["data"];
	$data_decode = base64_decode($data);

	$upload_dir = "./images/".$id."/".$img_name;

    if (!file_exists("./images/".$id)) {
        mkdir("./images/".$id, 0777, true);
    }
	
	try {
        file_put_contents($upload_dir, $data_decode); // save

        $reponse["message"] = "Upload Success";
		$reponse["satus"] = 200;
		$reponse["url"] = $upload_dir;

    } catch (Exception $e) {
        $reponse["message"] = "Upload Fail";
		$reponse["satus"] = 404;
    }	
}
else
{
	$reponse["message"] = "invalid request";
	$reponse["satus"] = 400;
}

echo json_encode($reponse);
?>