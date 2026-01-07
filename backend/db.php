<?php
$isProduction = getenv('RAILWAY_ENVIRONMENT') !== false || 
getenv('MYSQLHOST') !== false;

if ($isProduction) {
    $host = getenv('MYSQLHOST') ?: getenv('MYSQL_HOST');
    $port = getenv('MYSQLPORT') ?: getenv('MYSQL_PORT') ?: '3306';
    $dbname = getenv('MYSQLDATABASE') ?: getenv('MYSQL_DATABASE');
    $user = getenv('MYSQLUSER') ?: getenv('MYSQL_USER');
    $pass = getenv('MYSQLPASSWORD') ?: getenv('MYSQL_PASSWORD');
} else {
    $host = 'localhost';
    $port = '3306';
    $dbname = 'suaratangan';
    $user = 'root';
    $pass = ''; 
}

try {
    $pdo = new 
PDO("mysql:host=$host;port=$port;dbname=$dbname;charset=utf8mb4", $user, 
$pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
} catch (PDOException $e) {
    http_response_code(500);
    die(json_encode(["status" => "error", "message" => "Database 
connection failed"]));
}
?>
