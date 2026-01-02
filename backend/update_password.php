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
    $current_password = $_POST['current_password'] ?? '';
    $new_password = $_POST['new_password'] ?? '';
    $confirm_password = $_POST['confirm_password'] ?? '';

    // VALIDATION
    $errors = [];

    if (empty($user_id)) {
        $errors[] = "User ID is required";
    }

    if (empty($current_password)) {
        $errors[] = "Current password is required";
    }

    if (empty($new_password)) {
        $errors[] = "New password is required";
    }

    if (empty($confirm_password)) {
        $errors[] = "Password confirmation is required";
    }

    if ($new_password !== $confirm_password) {
        $errors[] = "New passwords do not match";
    }

    // Password validation: min 6 chars, 1 capital, 1 symbol
    if (!empty($new_password)) {
        if (strlen($new_password) < 6 ||
            !preg_match('/[A-Z]/', $new_password) ||
            !preg_match('/[^a-zA-Z0-9]/', $new_password)) {
            $errors[] = "Password must be 6+ characters with 1 capital and 
1 symbol";
        }
    }

    if (!empty($errors)) {
        echo json_encode(["status" => "error", "message" => implode(", ", 
$errors)]);
        exit;
    }

    try {
        // Verify current password
        $stmt = $pdo->prepare("SELECT password FROM users WHERE id = 
:id");
        $stmt->execute([':id' => $user_id]);
        $user = $stmt->fetch();

        if (!$user) {
            echo json_encode(["status" => "error", "message" => "User not 
found"]);
            exit;
        }

        if (!password_verify($current_password, $user['password'])) {
            echo json_encode(["status" => "error", "message" => "Current 
password is incorrect"]);
            exit;
        }

        // Update password
        $hashed_pass = password_hash($new_password, PASSWORD_BCRYPT);
        $stmt = $pdo->prepare("UPDATE users SET password = :pass WHERE id 
= :id");
        $stmt->execute([
            ':pass' => $hashed_pass,
            ':id' => $user_id
        ]);

        echo json_encode([
            "status" => "success",
            "message" => "Password updated successfully!"
        ]);
    } catch (PDOException $e) {
        error_log("Update password error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Password update failed"
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
