# Backend Setup Instructions

## Prerequisites
- XAMPP installed and running
- MySQL database `suaratangan` created in phpMyAdmin
- Users table with the following columns:
  - id (auto-increment primary key)
  - nama_lengkap (varchar)
  - username (varchar, unique)
  - email (varchar, unique)
  - telepon (varchar)
  - password (varchar)
  - peran (varchar)

## Installation Steps

1. Copy all PHP files (db.php, login.php, register.php) to your XAMPP htdocs directory.

   **Option A:** If placing directly in htdocs:
   ```
   C:\xampp\htdocs\backend\ (Windows)
   /Applications/XAMPP/htdocs/backend/ (Mac)
   ```

   **Option B:** If placing in a project folder:
   ```
   C:\xampp\htdocs\suaratangan\backend\ (Windows)
   /Applications/XAMPP/htdocs/suaratangan/backend/ (Mac)
   ```

2. Update the API URL in the Android app:
   - Open `app/src/main/java/com/example/utspab/ApiConfig.java`
   - Update `BASE_URL` to match your XAMPP server URL:
     - Option A: `http://192.168.0.101/backend/`
     - Option B: `http://192.168.0.101/suaratangan/backend/`

3. Ensure your computer's IP address matches the one in ApiConfig.java (currently: 192.168.0.101)

4. Make sure Apache and MySQL services are running in XAMPP Control Panel

5. Test the API endpoints:
   - Login: http://192.168.0.101/backend/login.php (or your path)
   - Register: http://192.168.0.101/backend/register.php (or your path)

## Note
- Make sure your Android device/emulator can reach the server IP address
- If using Android emulator, use `10.0.2.2` instead of `localhost`
- If using a physical device, ensure it's on the same network as your computer

