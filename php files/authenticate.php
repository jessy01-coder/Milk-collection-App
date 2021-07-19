<?php 


if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();

	$agnetNID=$connection->real_escape_string($_POST['nid']);
	$agnetPassword=$connection->real_escape_string($_POST['password']);

	$sql="SELECT `password` FROM `nyala_agents` WHERE `nid`='$agnetNID'";
	$result=$connection->query($sql);

	if ($result->num_rows>0) {
		while ($row=$result->fetch_assoc()) {
			$dbPassword=$row['password'];
		}
		if (password_verify($agnetPassword, $dbPassword)) {
			$JResponse['responseCode']="1";
			$JResponse['responseMessage']="Login successfully.";
			echo json_encode($JResponse);

		} else{
			$JResponse['responseCode']="0";
			$JResponse['responseMessage']="The password provided is invalid. Please confirm your password and try again.";
			echo json_encode($JResponse);

		}
	} else{
		$JResponse['responseCode']="0";
		$JResponse['responseMessage']="There's no account registered with this national identification number or your account was deleted. Please confirm and try again.";
		echo json_encode($JResponse);
	}
}

 ?>