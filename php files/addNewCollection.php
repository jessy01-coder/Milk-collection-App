<?php 

	function getConnection(){
	$serverName="localhost";
	$username="id16788618_nyaladairy";
	$password="3Qm_DPcgNN5&t2>\\";
	$db="id16788618_nyaladb";

		$connection=new Mysqli($serverName,$username,$password,$db);

		return $connection;
	}

	function updateGraphData($year, $capacity,$month){
	    $connection= getConnection();
		$sql="SELECT * FROM `nyala_report` WHERE `year`='$year'";
		$result=$connection->query($sql);

		if ($result->num_rows==0) {
		    $id=uniqid();
			$sql="INSERT INTO `nyala_report` (`id`,`year`,`$month`) VALUES ('$id','$year','$capacity')";
			$result=$connection->query($sql);
		}else{
			while ($row=$result->fetch_assoc()) {
				$dbCapacity=$row[$month];
			}
			$currentCapacity=$dbCapacity+$capacity;
			$sql="UPDATE `nyala_report` SET `$month`='$currentCapacity' WHERE `year`='$year'";
			$result=$connection->query($sql);
		}
		
	}

if($_SERVER['REQUEST_METHOD']=='POST'){

	$connection=getConnection();
	$JResponse=array();
	$collectionId=$connection->real_escape_string($_POST['id']);
	$farmerPhone=$connection->real_escape_string($_POST['phone']);
	$agnetNID=$connection->real_escape_string($_POST['agentNID']);
	$collectionCapacity=$connection->real_escape_string($_POST['capacity']);
	$collectionCash=$connection->real_escape_string($_POST['cash']);
	$collectionCanNumber=$connection->real_escape_string($_POST['canNumber']);
	$collectionShift=$connection->real_escape_string($_POST['shift']);
	$collectionDay=$connection->real_escape_string($_POST['day']);
	$collectionMonth=$connection->real_escape_string($_POST['month']);
	$collectionYear=$connection->real_escape_string($_POST['year']);
	$collectionTime=$connection->real_escape_string($_POST['time']);

	$sql="SELECT * FROM `nyala_farmer` WHERE `phone`='$farmerPhone'";
	$result=$connection->query($sql);
	if ($result->num_rows>0) {
	     while($row=$result->fetch_assoc()){
		    $farmerNID=$row['nid'];
		 }
		$sql="SELECT * FROM `nyala_collection` WHERE `id`='$collectionId'";
		$result=$connection->query($sql);
		if ($result->num_rows==0) {
    		$sql="SELECT * FROM `nyala_collection` WHERE `farmer_nid`='$farmerNID' AND `shift`='$collectionShift' AND `day`='$collectionDay' AND  `month`='$collectionMonth' AND `year`='$collectionYear'";
    		$result=$connection->query($sql);
    		if ($result->num_rows==0) {
    			$sql="INSERT INTO `nyala_collection`(`id`, `farmer_nid`, `agent_nid`, `capacity`, `cash`, `can_number`, `shift`, `day`, `month`, `year`, `time`) VALUES ('$collectionId','$farmerNID','$agnetNID','$collectionCapacity','$collectionCash','$collectionCanNumber','$collectionShift','$collectionDay','$collectionMonth','$collectionYear','$collectionTime')";
    			$result=$connection->query($sql);
    			if ($result>0) {
    			    updateGraphData($collectionYear, $collectionCapacity,$collectionMonth);
    			    $JResponse['responseCode']="1";
    			    $JResponse['responseMessage']="New collection added successfully!";
    			    echo json_encode($JResponse);
    			}else{
    				$JResponse['responseCode']="0";
    				$JResponse['responseMessage']="Sorry an internal error occurred. Check your internet connection and try again";
    				echo json_encode($JResponse);
    			}
    		}else{
    			$JResponse['responseCode']="0";
    			$JResponse['responseMessage']="There was a submission made already by the farmer for this shift please wait for the next shift of the day to submit the milk";
    			echo json_encode($JResponse);
		    }
		}else{
			$JResponse['responseCode']="0";
			$JResponse['responseMessage']="Sorry an internal error occurred please resubmit the data again";
			echo json_encode($JResponse);
		}
	}else{
			$JResponse['responseCode']="0";
			$JResponse['responseMessage']="There's no farmer registered to this National identification number. Please confirm and try again or less add the farmer into the system";
			echo json_encode($JResponse);
		}
}
?> 