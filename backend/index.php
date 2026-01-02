<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
echo json_encode([
    "status" => "success",
    "message" => "SuaraTangan API is running!",
    "endpoints" => [
        "register" => "/register.php",
        "login" => "/login.php"
    ]
]);
?>
