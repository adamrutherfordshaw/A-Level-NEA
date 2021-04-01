<?php
require "conn.php";
$username = $_POST["username"];
$password = $_POST["password"];
$employeeid = $_POST["employeeid"];
$fullname = $_POST["fullname"];
$admin = $_POST["admin"];
$mysql_qry = "insert into users_tbl (username, password, employeeid, fullname, administrator) values ('$username', '$password', '$employeeid', '$fullname', '$admin');";
if($conn->query($mysql_qry) === TRUE) {
	echo "Register successful";
}
else {
	echo "Register not successful. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>