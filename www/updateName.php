<?php
require "conn.php";
$oldusername = $_POST["username"];
$newusername = $_POST["newusername"];
$password = $_POST["password"];
$mysql_qry = "update users_tbl set username = '$newusername' where username like '$oldusername' and password like '$password';";
if($conn->query($mysql_qry) === FALSE) {
	echo "An error occurred. Please try again. Error: " . $mysql_qry . "<br>" . $conn->error;
}
else {
	echo "Updated name successfully";
}
$conn->close();