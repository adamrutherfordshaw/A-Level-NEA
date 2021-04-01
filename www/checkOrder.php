<?php
require "conn.php";
$customerid = $_POST["userid"];
$mysql_qry = "select orderid from orders_tbl where submitted like '0' and customerid like '$customerid';";
$result = mysqli_query($conn, $mysql_qry);
if(mysqli_num_rows($result) == 1) {
	$row = ($result->fetch_assoc());
	echo $row["orderid"];
} else if(mysqli_num_rows($result) == 0) {
	echo "No orders found";
}
$conn->close();
?>