<?php
require "conn.php";
$mysql_qry = "select upc, productname, aisle from products_tbl order by productname";
$result = mysqli_query($conn, $mysql_qry);
if ($conn->query($mysql_qry) == TRUE) {
	while($row = $result->fetch_assoc()) {
		echo $row["upc"] . "|" . $row["productname"] . "|" . $row["aisle"] . "\n";	
	}
} else {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>