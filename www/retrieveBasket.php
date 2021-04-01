<?php
require "conn.php";
$orderid = $_POST["orderid"];
$mysql_qry = "select products_tbl.productname, ordersitems_tbl.upc, quantity from products_tbl right join ordersitems_tbl on products_tbl.upc = ordersitems_tbl.upc right join orders_tbl on ordersitems_tbl.orderid = orders_tbl.orderid where orders_tbl.orderid = '$orderid' and submitted like '0';";
$result = mysqli_query($conn, $mysql_qry);
if ($conn->query($mysql_qry) == TRUE) {
	while($row = $result->fetch_assoc()) {
		if (($row["upc"] != "") and ($row["productname"] != "") and ($row["quantity"] != "")) {
			echo $row["upc"] . "|" . $row["productname"] . "|" . $row["quantity"] . "\n";
		}
	}
} else {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>