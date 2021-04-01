<?php
require "conn.php";
$username = $_POST["username"];
$password = $_POST["password"];
$mysql_qry = "select administrator from users_tbl where username like '$username' and password like '$password' and customer like '0';";
$result = mysqli_query($conn, $mysql_qry);
if(mysqli_num_rows($result) == 1) {
	$row = $result->fetch_assoc();
	echo "Login successful|" . $row["administrator"];
}
else {
	echo "Login not successful|Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>