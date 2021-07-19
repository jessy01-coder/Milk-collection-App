<?php 

if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();

	$farmerId=uniqid();
	$farmerName=$connection->real_escape_string($_POST['name']);
	$farmerNID=$connection->real_escape_string($_POST['nid']);
	$farmerPhone=$connection->real_escape_string($_POST['phone']);
	$farmerRoute=$connection->real_escape_string($_POST['route']);
	$agentNID=$connection->real_escape_string($_POST['agent_nid']);

	$sql="SELECT * FROM `nyala_farmer` WHERE `nid`='$farmerNID' OR `phone`='$farmerPhone'";
	$result=$connection->query($sql);
	if ($result->num_rows==0) {
		$sql="INSERT INTO `nyala_farmer`(`id`, `name`, `nid`, `phone`, `route`,`agent_nid`) VALUES ('$farmerId','$farmerName','$farmerNID','$farmerPhone','$farmerRoute','$agentNID')";
		$result=$connection->query($sql);
		if ($result>0) {
			$JResponse['responseCode']="1";
			$JResponse['responseMessage']="New farmer successfully added.";
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