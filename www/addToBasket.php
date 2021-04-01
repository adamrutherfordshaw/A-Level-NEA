<?php
require "conn.php";
$orderid = $_POST["orderid"];
$upc = $_POST["productid"];
$mysql_qry = "insert into ordersitems_tbl (orderid, upc, quantity) values ('$orderid', '$upc', '1');";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>