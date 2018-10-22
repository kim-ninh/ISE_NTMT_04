<?php
//check token
include "database.php";

function checkToken($token)
{
	$strQuery = 'SELECT FC_CHECKTOKEN("'.$token.'") AS RESULT';

	$conn = new database();
	$conn->connect();
	$result = $conn->query($strQuery);
	$check = false;
	foreach ($result as $row)
	{
		if ($row["RESULT"] == 1)
		{
			$check = true;
		}
		break;
	}
	$conn->disconnect();
	
	return $check;
}

?>