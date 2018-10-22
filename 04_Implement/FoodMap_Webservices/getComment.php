<?php 
//import library
include "../private/database.php";
$response = array();
	
if (isset($_GET['id_rest']))
{
	$id_rest = $_GET['id_rest'];
	//create class Comment
	class Comment{
		function Comment($date_time, $id_rest, $guest_email, $owner_email, $comment){
			$this->date_time = $date_time;
			$this->id_rest = $id_rest;
			$this->guest_email = $guest_email;
			$this->owner_email = $owner_email;
			$this->comment = $comment;
		}
	}
	
	//create connection
	$conn = new database();
	$conn->connect();

	//get result
	$listComments = $conn->GetComment($id_rest);

	if ($listComments != -1)
	{	
		$comments = array();
		foreach ($listComments as $row) {
			array_push($comments, new Comment($row['DATE_TIME'], $row['ID_REST'], $row['GUEST_EMAIL'], $row['OWNER_EMAIL'], $row['COMMENT']));
		}

		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $comments;
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Exec fail";
	}
	
	//close conn
	$conn->disconnect();
}
else
{
	$response["status"] = 400;
	$response["message"] = "Invalid request";
}
echo json_encode($response);
?>