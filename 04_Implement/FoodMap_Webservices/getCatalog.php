<?php  
include "../private/database.php";

class Catalog 
{
	
	function Catalog($id, $name)
	{
		$this->id = $id;
		$this->name = $name;
	}
}

$response = array();

$conn = new database();
$conn->connect();


$listCatalogs = $conn->GetCatalog();
if ($listCatalogs != -1)
{
	$response["status"] = 200;
	$response["message"] = "Success";
	$catalogs = array();

	foreach ($listCatalogs as $row) {
		array_push($catalogs, new Catalog($row["ID"], $row["NAME"]));
	}
	$response["data"] = $catalogs;
}
else
{
	$response["status"] = 404;
	$response["message"] = "Exec Fail";
}

$conn->disconnect();

echo json_encode($response);
?>