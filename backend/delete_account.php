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

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $user_id = $_POST['user_id'] ?? '';
    $password = $_POST['password'] ?? '';

    if (empty($user_id)) {
        echo json_encode(["status" => "error", "message" => "User ID is 
required"]);
        exit;
    }

    if (empty($password)) {
        echo json_encode(["status" => "error", "message" => "Password is 
required to delete account"]);
        exit;
    }

    try {
        // Verify password before deleting
        $stmt = $pdo->prepare("SELECT password FROM users WHERE id = 
:id");
        $stmt->execute([':id' => $user_id]);
        $user = $stmt->fetch();

        if (!$user) {
            echo json_encode(["status" => "error", "message" => "User not 
found"]);
            exit;
        }

        if (!password_verify($password, $user['password'])) {
            echo json_encode(["status" => "error", "message" => "Incorrect 
password"]);
            exit;
        }

        // Delete user
        $stmt = $pdo->prepare("DELETE FROM users WHERE id = :id");
        $stmt->execute([':id' => $user_id]);

        echo json_encode([
            "status" => "success",
            "message" => "Account deleted successfully"
        ]);
    } catch (PDOException $e) {
        error_log("Delete account error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Account deletion failed"
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
