<?php
require "conn.php";
$orderid = $_POST["orderid"];
$upc = $_POST["upc"];
$mysql_qry = "update ordersitems_tbl set picked = '1' where orderid like '$orderid' and upc like '$upc';";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
?>