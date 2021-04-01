<?php
require "conn.php";
$username = $_POST["username"];
$password = $_POST["password"];
$email = $_POST["email"];
$fullname = $_POST["fullname"];
$dateofbirth = $_POST["dateofbirth"];
$mysql_qry = "insert into users_tbl (fullname, emailaddress, dateofbirth, username, password, customer) values ('$fullname', '$email', '$dateofbirth', '$username', '$password', '1');";
if($conn->query($mysql_qry) === TRUE) {
	echo "Register successful";
}
else {
	echo "Register not successful. Error: " . $mysql_qry . "<br>" . $conn->error;
}
$conn->close();
?>