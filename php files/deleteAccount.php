<?php 

if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();
	$userNID=$connection->real_escape_string($_POST['nid']);

	$sql="DELETE FROM `nyala_agents` WHERE `nid`='$userNID'";
	$response=$connection->query($sql);
	if ($response->num_rows>0) {
		$JResponse['responseCode']="1";
		$JResponse['responseMessage']="Account deleted.Thank you for choosing Nyala Dairy";
		echo json_encode($JResponse);
	}else{
		$JResponse['responseCode']="0";
		$JResponse['responseMessage']="Failed to delete account. Retry";
		echo json_encode($JResponse);
	}
}

 ?>