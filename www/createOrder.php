<?php
require "conn.php";
$customerid = $_POST["userid"];
$mysql_qry = "insert into orders_tbl (customerid) values ('$customerid');";
if($conn->query($mysql_qry) == TRUE) {
	$mysql_qry = "select orderid from orders_tbl where customerid like '$customerid' and submitted like '0';";
	$result = mysqli_query($conn, $mysql_qry);
	if (mysqli_num_rows($result) == 1) {
		$row = $result->fetch_assoc();
		echo $row["orderid"];
	} else {
		echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
	}
} else {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>