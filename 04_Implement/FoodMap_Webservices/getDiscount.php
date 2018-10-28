<?php  
include '../private/database.php';

class Discount{
	Discount($id, $id_rest, $namedish, $discount_percent, $timestart, $timeend){
		$this->id = $id;
		$this->id_rest = $id_rest;
		$this->namedish = $namedish;
		$this->discount_percent = $discount_percent;
		$this->timestart = $timestart;
		$this->timeend = $timeend;
	}
}


$response = array();

if (isset($_GET["id_rest"]))
{
	$conn = new database();
	$conn->connect();
	$listDiscount = $conn->GetDiscount($_GET["id_rest"]);

	if ($listDiscount != -1)
	{
		$discounts = array();
		foreach ($listDiscount as $row) {
			array_push($discounts, new Discount($row["ID"], $row["ID_REST"], $row["NAMEDISH"], $row["DISCOUNT_PERCENT"], $row["TIMESTART"], $row["TIMEEND"]));
		}
		
		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $discounts;
	}
	else
	{
		$response["status"] = 404;
		$response["message"] = "Exec fail";
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