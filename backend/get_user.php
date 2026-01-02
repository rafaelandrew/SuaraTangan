<?php
error_reporting(E_ALL);
ini_set('display_errors', 0);
ini_set('log_errors', 1);

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST' || $_SERVER['REQUEST_METHOD'] 
=== 'GET') {
    $user_id = $_POST['user_id'] ?? $_GET['user_id'] ?? '';

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID is 
required"]);
        exit;
    }

    try {
        $stmt = $pdo->prepare("SELECT id, nama_lengkap, username, email, 
telepon, peran FROM users WHERE id = :id");
        $stmt->execute([':id' => $user_id]);
        $user = $stmt->fetch();

        if ($user) {
            echo json_encode([
                "status" => "success",
                "user" => $user
            ]);
        } else {
            echo json_encode([
                "status" => "error",
                "message" => "User not found"
            ]);
        }
    } catch (PDOException $e) {
        error_log("Get user error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Failed to retrieve user data"
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
