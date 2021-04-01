<?php
require "conn.php";
$orderid = $_POST["orderid"];
$mysql_qry = "select productname, products_tbl.upc, quantity, aisle, location from products_tbl right join ordersitems_tbl on products_tbl.upc = ordersitems_tbl.upc where orderid like '$orderid' and picked like '0';";
$result = mysqli_query($conn, $mysql_qry);
if ($conn->query($mysql_qry) == TRUE) {
	while($row = $result->fetch_assoc()) {
		$productname = $row["productname"];
		$upc = $row["upc"];
		$quantity = $row["quantity"];
		$aisle = $row["aisle"];
		$location = $row["location"];
		echo $productname . "|" . $upc . "|" . $quantity . "|" . $aisle . "|" . $location . "\n";
	}
} else {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>