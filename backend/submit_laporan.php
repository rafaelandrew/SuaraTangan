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
    $judul = trim($_POST['judul'] ?? '');
    $deskripsi = trim($_POST['deskripsi'] ?? '');

    // Validation
    $errors = [];

    if (empty($user_id)) {
        $errors[] = "User ID is required";
    }

    if (empty($judul)) {
        $errors[] = "Judul masalah is required";
    }

    if (empty($deskripsi)) {
        $errors[] = "Deskripsi masalah is required";
    }

    if (!empty($errors)) {
        echo json_encode(["status" => "error", "message" => implode(", ", 
$errors)]);
        exit;
    }

    try {
        $sql = "INSERT INTO laporan_masalah (user_id, judul, deskripsi) 
VALUES (:user_id, :judul, :deskripsi)";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            ':user_id' => $user_id,
            ':judul' => $judul,
            ':deskripsi' => $deskripsi
        ]);

        echo json_encode([
            "status" => "success",
            "message" => "Laporan berhasil dikirim!",
            "laporan_id" => $pdo->lastInsertId()
        ]);
    } catch (PDOException $e) {
        error_log("Submit laporan error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Failed to submit report"
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
