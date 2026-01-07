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
    $saran = trim($_POST['saran'] ?? '');

    $errors = [];

    if (empty($user_id)) {
        $errors[] = "User ID is required";
    }

    if (empty($saran)) {
        $errors[] = "Saran is required";
    }

    if (!empty($errors)) {
        echo json_encode(["status" => "error", "message" => implode(", ", 
$errors)]);
        exit;
    }

    try {
        $sql = "INSERT INTO kotak_saran (user_id, saran) VALUES (:user_id, 
:saran)";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            ':user_id' => $user_id,
            ':saran' => $saran
        ]);

        echo json_encode([
            "status" => "success",
            "message" => "Terima kasih! Masukan Anda telah kami terima.",
            "saran_id" => $pdo->lastInsertId()
        ]);
    } catch (PDOException $e) {
        error_log("Submit saran error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Failed to submit suggestion"
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
