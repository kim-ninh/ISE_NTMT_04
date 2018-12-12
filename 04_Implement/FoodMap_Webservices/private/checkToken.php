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

function checkTokenForRestaurant($id_rest, $token)
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

	// kiểm tra xem id_rest có thuộc về tài khoản này không
	if ($check)
	{
		$username = substr($token, 32);
		$username = base64_decode($username);
		$check = $conn->CheckExitsRestaurant($id_rest, $username);
	}

	$conn->disconnect();
	return $check;
}

function checkTokenForUsername($username, $token)
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

	// kiểm tra xem tài khoản này có thuộc về token này không
	if ($check)
	{
		$usernameCheck = substr($token, 32);
		$usernameCheck = base64_decode($usernameCheck);
		$check = $username == $usernameCheck;
	}

	$conn->disconnect();
	return $check;
}

function checkTokenForDiscount($id_discount, $token)
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

	// kiểm tra xem discount này có thuộc về token này không
	if ($check)
	{
		$username = substr($token, 32);
		$username = base64_decode($usernameCheck);
		
		$strQuery = 'SELECT RS.OWNER_USERNAME AS USERNAME FROM RESTAURANT RS JOIN DISCOUNT DS ON RS.ID = DS.ID_REST WHERE DS.ID = '.$id_discount;
		$usernameRow = $conn->query($strQuery);
		
		if ($username != -1)
		{
			$usernameCheck = "";
			foreach ($usernameRow as $row)
			{
				$usernameCheck = $usernameRow["USERNAME"];
				break;
			}

			$check = $username == $usernameCheck;
		}
		else
		{
			$check = false;
		}
	}
	$conn->disconnect();
	return $check;
}


?>