<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

echo json_encode([
    "status" => "success",
    "message" => "Test successful!",
    "method" => $_SERVER['REQUEST_METHOD'],
    "post_data" => $_POST,
    "raw_input" => file_get_contents('php://input')
]);
?>
