<?php

$reponse = array();

function url(){
    if(isset($_SERVER['HTTPS'])){
        $protocol = ($_SERVER['HTTPS'] && $_SERVER['HTTPS'] != "off") ? "https" : "http";
    }
    else{
        $protocol = 'http';
    }
    return $protocol . "://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];
}

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
	
    if (file_exists($upload_dir)){
    	unlink($upload_dir); //xóa file cũ
    }

	try {
        file_put_contents($upload_dir, $data_decode); // save

        $reponse["message"] = "Upload Success";
		$reponse["status"] = 200;
		$reponse["url"] = url() . $upload_dir;

    } catch (Exception $e) {
        $reponse["message"] = "Upload Fail";
		$reponse["status"] = 404;
    }	
}
else
{
	$reponse["message"] = "invalid request";
	$reponse["status"] = 400;
}

echo json_encode($reponse);
?>