<?php 

$serverName="localhost";
$username="root";
$password="";
$db="nyala_dairy";

$connection=new Mysqli($serverName,$username,$password,$db);
   echo "connected";
 ?>