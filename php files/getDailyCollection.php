<?php 


if($_SERVER['REQUEST_METHOD']=='POST'){

	require 'config.php';
	$JResponse=array();
	$dataArray=array();

	$curretDay=$connection->real_escape_string($_POST['day']);
	$currentMonth=$connection->real_escape_string($_POST['month']);
	$currentYear=$connection->real_escape_string($_POST['year']);
	$agentNID=$connection->real_escape_string($_POST['nid']);

	$sql="SELECT 
	nyala_farmer.name,
	nyala_collection.capacity,
	nyala_collection.id,
	nyala_collection.time,
	nyala_collection.farmer_nid 

	FROM `nyala_collection` INNER JOIN `nyala_farmer` ON nyala_farmer.nid = nyala_collection.farmer_nid WHERE nyala_collection.day='$curretDay' AND nyala_collection.month='$currentMonth' AND nyala_collection.year='$currentYear' AND nyala_collection.agent_nid='$agentNID'";
	
	$result=$connection->query($sql);
	if ($result->num_rows>0) {
		while ($row=$result->fetch_assoc()) {
			array_push($dataArray, $row);
		}

		$JResponse['responseCode']="1";
		$JResponse['responseData']=$dataArray;

		$sql="SELECT COUNT(*) FROM `nyala_collection` WHERE `day`='$curretDay' AND `month`='$currentMonth' AND `year`='$currentYear' AND `agent_nid`='$agentNID'";
		$result=$connection->query($sql);
		while ($row=$result->fetch_assoc()) {
			$JResponse['responseCount']=$row['COUNT(*)'];
		}

		$sql="SELECT SUM(`capacity`) FROM `nyala_collection` WHERE `day`='$curretDay' AND `month`='$currentMonth' AND `year`='$currentYear' AND `agent_nid`='$agentNID'";
		$result=$connection->query($sql);
		while ($row=$result->fetch_assoc()) {
			$JResponse['responseTotal']=$row['SUM(`capacity`)'];
		}
		
		echo json_encode($JResponse);
	}else{
		$JResponse['responseCode']="0";
		$JResponse['responseMessage']="No daily collections made so far";
		echo json_encode($JResponse);
	}

}




?>