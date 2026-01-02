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
    $nama = trim($_POST['nama_lengkap'] ?? '');
    $telp = trim($_POST['telepon'] ?? '');
    $peran = $_POST['peran'] ?? '';

    // VALIDATION
    $errors = [];

    if (empty($user_id)) {
        $errors[] = "User ID is required";
    }

    // 1. Nama must start with capital letter
    if (!empty($nama) && !preg_match('/^[A-Z]/', $nama)) {
        $errors[] = "Nama must start with a capital letter";
    }

    // 2. Telepon must be exactly 12 digits if provided
    if (!empty($telp) && !preg_match('/^\d{12}$/', $telp)) {
        $errors[] = "Telepon must be exactly 12 digits";
    }

    // 3. Peran must be one of the two options if provided
    if (!empty($peran)) {
    error_log("Received peran: [" . $peran . "] length: " . strlen($peran) 
. " hex: " . bin2hex($peran));
    error_log("Check Teman Dengar: " . ($peran === 'Teman Dengar' ? 'YES' 
: 'NO'));
    error_log("Check Teman Tuli: " . ($peran === 'Teman Tuli' ? 'YES' : 
'NO'));
    
    if (!in_array($peran, ['Teman Dengar', 'Teman Tuli'], true)) {
        $errors[] = "Peran must be 'Teman Dengar' or 'Teman Tuli'";
    }
}

    if (!empty($errors)) {
        echo json_encode(["status" => "error", "message" => implode(", ", 
$errors)]);
        exit;
    }

    try {
        // Build dynamic SQL based on what fields are provided
        $updates = [];
        $params = [':id' => $user_id];

        if (!empty($nama)) {
            $updates[] = "nama_lengkap = :nama";
            $params[':nama'] = $nama;
        }
        if (!empty($telp)) {
            $updates[] = "telepon = :telp";
            $params[':telp'] = $telp;
        }
        if (!empty($peran)) {
            $updates[] = "peran = :peran";
            $params[':peran'] = $peran;
        }

        if (empty($updates)) {
            echo json_encode(["status" => "error", "message" => "No fields 
to update"]);
            exit;
        }

        $sql = "UPDATE users SET " . implode(", ", $updates) . " WHERE id 
= :id";
        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);

        if ($stmt->rowCount() > 0) {
            // Fetch updated user data
            $stmt = $pdo->prepare("SELECT id, nama_lengkap, username, 
email, telepon, peran FROM users WHERE id = :id");
            $stmt->execute([':id' => $user_id]);
            $user = $stmt->fetch();

            echo json_encode([
                "status" => "success",
                "message" => "Profile updated successfully!",
                "user" => $user
            ]);
        } else {
            echo json_encode([
                "status" => "error",
                "message" => "No changes made or user not found"
            ]);
        }
    } catch (PDOException $e) {
        error_log("Update profile error: " . $e->getMessage());
        echo json_encode([
            "status" => "error",
            "message" => "Update failed: " . $e->getMessage()
        ]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?> 
