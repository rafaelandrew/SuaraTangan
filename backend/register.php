<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
require 'db.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nama = trim($_POST['nama_lengkap'] ?? '');
    $user = trim($_POST['username'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $telp = trim($_POST['telepon'] ?? '');
    $pass = $_POST['password'] ?? '';
    $peran = $_POST['peran'] ?? '';

    $errors = [];

    if (!preg_match('/^[A-Z]/', $nama)) {
        $errors[] = "Nama must start with a capital letter";
    }

    if (strlen($user) < 4 || strlen($user) > 16) {
        $errors[] = "Username must be 4-16 characters";
    }

    if (!filter_var($email, FILTER_VALIDATE_EMAIL) || 
!str_contains($email, '.com')) {
        $errors[] = "Email must be valid and contain .com";
    }

    if (!preg_match('/^\d{12}$/', $telp)) {
        $errors[] = "Telepon must be exactly 12 digits";
    }

    if (strlen($pass) < 6 || 
        !preg_match('/[A-Z]/', $pass) || 
        !preg_match('/[^a-zA-Z0-9]/', $pass)) {
        $errors[] = "Password must be 6+ characters with 1 capital and 1 
symbol";
    }

    if (!in_array($peran, ['Teman Dengar', 'Teman Tuli'])) {
        $errors[] = "Peran must be 'Teman Dengar' or 'Teman Tuli'";
    }

    if (!empty($errors)) {
        echo json_encode(["status" => "error", "message" => implode(", ", 
$errors)]);
        exit;
    }

    $hashed_pass = password_hash($pass, PASSWORD_BCRYPT);

    try {
        $sql = "INSERT INTO users (nama_lengkap, username, email, telepon, 
password, peran) 
                VALUES (:nama, :user, :email, :telp, :pass, :peran)";
        $stmt = $pdo->prepare($sql);
        
        $stmt->execute([
            ':nama' => $nama,
            ':user' => $user,
            ':email' => $email,
            ':telp' => $telp,
            ':pass' => $hashed_pass,
            ':peran' => $peran
        ]);

        echo json_encode(["status" => "success", "message" => "User 
registered successfully!"]);
    } catch (PDOException $e) {
        if (str_contains($e->getMessage(), 'Duplicate entry') || 
str_contains($e->getMessage(), 'duplicate key')) {
            echo json_encode(["status" => "error", "message" => "Username 
or email already exists"]);
        } else {
            echo json_encode(["status" => "error", "message" => 
"Registration failed: " . $e->getMessage()]);
        }
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request 
method"]);
}
?>
