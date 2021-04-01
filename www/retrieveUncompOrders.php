<?php
require "conn.php";
$mysql_qry = "select orderid, orderdate from orders_tbl where completed like '0' and submitted like '1' order by orderdate;";
$result = mysqli_query($conn, $mysql_qry);
if ($conn->query($mysql_qry) == TRUE) {
	while($row = $result->fetch_assoc()) {
		$orderid = $row["orderid"];
		$orderdate = $row["orderdate"];
		$count_qry = "select count(*) as count from ordersitems_tbl where orderid like '$orderid' and picked like '0';";
		$countresult = mysqli_query($conn, $count_qry);
		if ($conn->query($count_qry) == TRUE) {
			$countrow = $countresult->fetch_assoc();
			$count = $countrow["count"];
			echo $orderid . "|" . $orderdate . "|" . $count . "\n";
		} else {
			echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
		}
	}
} else {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
?>