<?php  
include '../private/database.php';

class Offer{
	function Offer($namedish, $discount_percent, $guest_email, $total){
		$this->namedish = $namedish;
		$this->discount_percent = $discount_percent;
		$this->guest_email = $guest_email;
		$this->total = $total;
	}
}

$response = array();

if (isset($_GET["id_rest"]))
{
	$conn = new database();
	$conn->connect();
	$listOffer = $conn->GetOffer($_GET["id_rest"]);

	if ($listOffer != -1)
	{
		$offers = array();
		foreach ($listOffer as $row) {
			array_push($offers, new Offer($row["NAMEDISH"], $row["DISCOUNT_PERCENT"], $row["GUEST_EMAIL"], $row["TOTAL"]));
		}
		
		$response["status"] = 200;
		$response["message"] = "Success";
		$response["data"] = $offers;
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