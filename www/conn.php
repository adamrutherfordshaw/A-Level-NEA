<?php
$db_name = "asdac&c";
$mysql_username = "root";
$mysql_password = "@XHfELFEsGZG7c6";
$server_name = "localhost";
$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);
if (!$conn) {
	echo "Connection failed";
}
?>