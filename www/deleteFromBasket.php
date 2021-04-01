<?php
require "conn.php";
$orderid = $_POST["orderid"];
$upc = $_POST["productid"];
$mysql_qry = "delete from ordersitems_tbl where orderid like '$orderid' and upc like '$upc';";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
?>