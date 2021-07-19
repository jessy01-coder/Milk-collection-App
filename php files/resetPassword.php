<?php 

if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();
	
	$userNID=$connection->real_escape_string($_POST['nid']);
	$password=$connection->real_escape_string($_POST['password']);

	$encryptedPassword=password_hash($password, PASSWORD_DEFAULT);

		$sql="UPDATE `nyala_agents` SET `password`='$encryptedPassword' WHERE `nid`='$userNID'";
		$response=$connection->query($sql);
		$response=$connection->query($sql);
		if ($response>0) {
			$JResponse['responseCode']="1";
			$JResponse['responseMessage']="Password reset was successful";
			echo json_encode($JResponse);
		}else{
			$JResponse['responseCode']="0";
			$JResponse['responseMessage']="Sorry an internal error occurred. Please confirm your internet connection and try again";
			echo json_encode($JResponse);
		}
}

 ?>