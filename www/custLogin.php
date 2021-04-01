<?php
require "conn.php";
$username = $_POST["username"];
$password = $_POST["password"];
$mysql_qry = "select userid from users_tbl where username like '$username' and password like '$password' and customer like '1';";
$result = mysqli_query($conn, $mysql_qry);
if(mysqli_num_rows($result) == 1) {
	echo "Login successful" . "|";
	$row = ($result->fetch_assoc());
	$custid = $row["userid"];
	echo $custid . "|";
	$POSTorderid_qry = "select orders_tbl.orderid from orders_tbl left join ordersitems_tbl on orders_tbl.orderid = ordersitems_tbl.orderid where customerid like '$custid' and submitted like '0' and ordersitems_tbl.orderid is null;";
	$orderidresult = mysqli_query($conn, $POSTorderid_qry);
	if (mysqli_num_rows($orderidresult) > 0) {
		$row = $orderidresult->fetch_assoc();
		$orderid = $row["orderid"];
		$deleteorder_qry = "delete from orders_tbl where orderid like '$orderid' and submitted like '0';";
		if($conn->query($deleteorder_qry) === TRUE) {
			echo "Completed";
		}
	}
} else {
	echo "Login not successful. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>