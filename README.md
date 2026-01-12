# 🤝 Suara Tangan

A mobile application that bridges communication between the deaf/hard of hearing community and hearing individuals in Indonesia using Machine Learning to translate BISINDO (Indonesian Sign Language) in real-time.

This is a demo project developed as part of an academic course to showcase the potential features and functionalities of a sign language translation app. The Machine Learning models and translation features are prototypes and not fully implemented. This proje ct serves as a proof-of-concept to demonstrate what such an application could achieve with further development and proper ML model training.

## ✨ Features

- **Real-time Translation**: Convert BISINDO gestures to text using camera input
- **Speech-to-Text**: Translate spoken Indonesian to text for deaf users
- **Video Translation**: Upload and analyze sign language videos
- **User Management**: Complete account system with profile management
- **Help Center**: Comprehensive FAQ, privacy policy, and user guides
- **Feedback System**: Report issues and submit suggestions
- **Notifications**: Stay updated with app news and features

## 🛠️ Tech Stack

**Frontend:**
- Android (Java)
- Android Volley (HTTP requests)
- Material Design Components
- SharedPreferences (Session management)

**Backend:**
- PHP
- MySQL Database
- PDO (Prepared Statements)
- bcrypt (Password hashing)
- RESTful API

**Development Tools:**
- Android Studio
- XAMPP (Apache + MySQL)

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- XAMPP
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/suara-tangan.git
cd suara-tangan
```

### 2. Setup XAMPP Database

1. **Navigate to XAMPP's htdocs folder:**
   - Open File Explorer
   - Go to `xampp/htdocs/`
   - Create a new folder named `suaratangan`

2. **Copy backend files:**
   - Copy all files from the `backend` folder (from the cloned repository)
   - Paste them into `xampp/htdocs/suaratangan/`

3. **Start XAMPP services:**
   - Open XAMPP Control Panel
   - Start **Apache** and **MySQL** modules

4. **Create the database:**
   - Open your browser and go to `http://localhost/phpmyadmin`
   - Click "New" to create a new database
   - Name it `suaratangan`
   - Click "Create"

5. **Import database schema:**
   - Select the `suaratangan` database you just created
   - Click on the "Import" tab
   - Choose the `suaratangan.sql` file from the repository
   - Click "Go" to import

6. **Verify backend connection:**
   - Find your local IP address:
     - Windows: Open CMD and type `ipconfig`, look for IPv4 Address
     - Mac: Open Terminal and type `ifconfig`, look for inet address
   - Open browser and go to `http://YOUR_IP_ADDRESS/suaratangan/login.php`
   - You should see an error message: `{"status":"error","message":"Invalid request method"}`
   - This confirms the backend is running correctly!

### 3. Configure Android Studio

1. **Open the project:**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository folder

2. **Update API configuration:**
   - In Android Studio, navigate to `app/src/main/java/com/example/suaratangan/config/ApiConfig.java`
   - On **line 4**, update the BASE_URL with your IP address:
   
   ```java
   public static final String BASE_URL = "http://YOUR_IP_ADDRESS/suaratangan/";
   ```
   
   Replace `YOUR_IP_ADDRESS` with the IP you found in step 6 above

3. **Sync Gradle:**
   - Click "Sync Now" if prompted
   - Wait for Gradle to finish syncing

4. **Run the app:**
   - Connect your Android device via USB (with USB debugging enabled) or start an emulator
   - Click the "Run" button (green play icon) in Android Studio
   - Select your device/emulator
   - Wait for the app to install and launch

5. **Test the app:**
   - Try registering a new account
   - Login with your credentials
   - Explore the features!

## 🔧 Troubleshooting

- **Cannot connect to database:** Make sure Apache and MySQL are running in XAMPP
- **Network error in app:** Verify your IP address is correct in `ApiConfig.java` and your phone/emulator is on the same network
- **Gradle sync failed:** Try "File → Invalidate Caches / Restart" in Android Studio

## 👥 Contributors
This project is part of the Mobile Application Programming (SIP 118) course final project, developed by:
- Runi Dwi Jiasta (202304560001)
- Teresa Kaena Dharmanyoto (202304560014)
- Caroline Evarista Den Lau (202304560027)
- Andrew Riza Rafhael (202304560030)
---
