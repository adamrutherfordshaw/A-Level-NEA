<?php
require "conn.php";
$username = $_POST["username"];
$password = $_POST["password"];
$mysql_qry = "select fullname from users_tbl where username like '$username' and password like '$password'";
$result = mysqli_query($conn, $mysql_qry);
$row = mysqli_fetch_array($result);
echo $row['fullname'];;
?>