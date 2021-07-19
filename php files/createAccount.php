<?php 

if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();

	$agnetId=uniqid();
	$agnetName=$connection->real_escape_string($_POST['name']);
	$agnetNID=$connection->real_escape_string($_POST['nid']);
	$agnetPhone=$connection->real_escape_string($_POST['phone']);
	//$agnetRoute=$connection->real_escape_string($_POST['route']);
	$agnetPassword=$connection->real_escape_string($_POST['password']);

	$sql="SELECT * FROM `nyala_agents` WHERE `nid`='$agnetNID' OR `phone`='$agnetPhone'";
	$result=$connection->query($sql);
	if ($result->num_rows==0) {
		$encryptedPassword=password_hash($agnetPassword, PASSWORD_DEFAULT);
		$sql="INSERT INTO `nyala_agents`(`id`, `name`, `nid`, `phone`,`password`) VALUES ('$agnetId','$agnetName','$agnetNID','$agnetPhone','$encryptedPassword')";
		$result=$connection->query($sql);
		if ($result>0) {
			$JResponse['responseCode']="1";
			$JResponse['responseMessage']="Registration successful. Welcome to Nyala Dairy.";
			echo json_encode($JResponse);
		}else{
			$JResponse['responseCode']="0";
			$JResponse['responseMessage']="Sorry an internal error occurred. Please confirm your details and try again.";
			echo json_encode($JResponse);
		}
	}else{
		$JResponse['responseCode']="0";
		$JResponse['responseMessage']="There's an account with this phone/National Id No. Please confirm and try again.";
		echo json_encode($JResponse);
	}
}

	
?> 