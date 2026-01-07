<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $credential = $_POST['credential'] ?? '';
    $pass = $_POST['password'] ?? '';

    try {
        $stmt = $pdo->prepare("SELECT * FROM users WHERE email = :cred OR 
username = :cred");
        $stmt->execute([':cred' => $credential]);
        $user = $stmt->fetch();

        if ($user && password_verify($pass, $user['password'])) {
            unset($user['password']);
            echo json_encode([
                "status" => "success", 
                "message" => "Login successful",
                "user" => $user
            ]);
        } else {
            echo json_encode(["status" => "error", "message" => "Invalid 
username or password"]);
        }
    } catch (PDOException $e) {
        echo json_encode(["status" => "error", "message" => "Database 
error: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>


