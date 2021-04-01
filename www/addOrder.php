<?php
require "conn.php";
$orderid = $_POST["orderid"];
$date = date('Y-m-d');
$mysql_qry = "update orders_tbl set submitted = '1', orderdate = '$date' where orderid like '$orderid';";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close(); 
?>