<?php
require "conn.php";
$orderid = $_POST["orderid"];
$username = $_POST["username"];
$mysql_qry = "select employeeid from users_tbl where username like '$username'";
$result = mysqli_query($conn, $mysql_qry);
$row = mysqli_fetch_array($result);
$colleagueid = $row['employeeid'];
$mysql_qry = "update orders_tbl set completed = '1', colleagueid = '$colleagueid' where orderid like '$orderid';";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>