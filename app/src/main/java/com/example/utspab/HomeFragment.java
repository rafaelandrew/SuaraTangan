package com.example.utspab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    // --- VARIABEL UI ---
    private RelativeLayout layoutStartOverlay, layoutActiveSession;
    private Button btnStartSession;
    private ImageButton btnCloseSession;

    private PreviewView viewFinder;
    private VideoView videoPreview;
    private TextView tvTranslationResult, tvInstruction;
    private ImageView btnMainAction;
    private ImageButton btnModeCamera, btnModeMic, btnModeVideo;

    // --- VARIABEL LOGIKA ---
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;

    // Kode Request untuk Mic
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    // Launcher Galeri Video
    private final ActivityResultLauncher<String> videoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) playVideo(uri);
            }
    );

    // Status Mode
    private enum AppMode { CAMERA, MIC, VIDEO }
    private AppMode currentMode = AppMode.CAMERA;

    private boolean isDetectingGestures = false;
    private long lastUpdateTime = 0;
    private final String[] dummyVocabulary = {"Halo", "Nama", "Saya", "A", "B", "C", "Terima Kasih"};
    private final String DEFAULT_TEXT = "Hasil terjemahan akan muncul di sini...";

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Init View
        layoutStartOverlay = view.findViewById(R.id.layout_start_overlay);
        layoutActiveSession = view.findViewById(R.id.layout_active_session);
        btnStartSession = view.findViewById(R.id.btn_start_session);
        btnCloseSession = view.findViewById(R.id.btn_close_session);

        viewFinder = view.findViewById(R.id.viewFinder);
        videoPreview = view.findViewById(R.id.videoPreview);
        tvTranslationResult = view.findViewById(R.id.tv_translation_result);
        tvInstruction = view.findViewById(R.id.tv_instruction);
        btnMainAction = view.findViewById(R.id.btn_main_action);

        btnModeCamera = view.findViewById(R.id.btn_mode_camera);
        btnModeMic = view.findViewById(R.id.btn_mode_mic);
        btnModeVideo = view.findViewById(R.id.btn_mode_video);

        // 2. Setup Listener
        btnStartSession.setOnClickListener(v -> checkPermissionAndStart());
        btnCloseSession.setOnClickListener(v -> stopSession());

        btnModeCamera.setOnClickListener(v -> switchMode(AppMode.CAMERA));
        btnModeMic.setOnClickListener(v -> switchMode(AppMode.MIC));
        btnModeVideo.setOnClickListener(v -> switchMode(AppMode.VIDEO));
        btnMainAction.setOnClickListener(v -> handleMainButtonAction());

        cameraExecutor = Executors.newSingleThreadExecutor();

        return view;
    }

    // --- LOGIKA SESI ---

    private void checkPermissionAndStart() {
        if (allPermissionsGranted()) {
            startSession();
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startSession() {
        layoutStartOverlay.setVisibility(View.GONE);
        layoutActiveSession.setVisibility(View.VISIBLE);
        startCamera();
        switchMode(AppMode.CAMERA);
    }

    private void stopSession() {
        stopGestureDetection();
        if (videoPreview != null && videoPreview.isPlaying()) videoPreview.stopPlayback();
        if (cameraProvider != null) cameraProvider.unbindAll();

        tvTranslationResult.setText(DEFAULT_TEXT);
        layoutActiveSession.setVisibility(View.GONE);
        layoutStartOverlay.setVisibility(View.VISIBLE);
    }

    // --- LOGIKA GANTI MODE ---

    private void switchMode(AppMode mode) {
        stopGestureDetection();
        tvTranslationResult.setText(DEFAULT_TEXT);

        // Reset Video & Camera View
        if (videoPreview != null) {
            videoPreview.stopPlayback();
            videoPreview.setVisibility(View.GONE);
        }
        if (viewFinder != null) viewFinder.setVisibility(View.VISIBLE);

        this.currentMode = mode;
        updateUIForMode(mode);

        if (mode == AppMode.VIDEO) {
            if (cameraProvider != null) cameraProvider.unbindAll();
            if (viewFinder != null) viewFinder.setVisibility(View.GONE);
            if (videoPreview != null) videoPreview.setVisibility(View.VISIBLE);
        } else if (mode == AppMode.MIC) {
            if (cameraProvider != null) cameraProvider.unbindAll();
            if (viewFinder != null) viewFinder.setVisibility(View.GONE);
        } else {
            startCamera();
        }
    }

    private void updateUIForMode(AppMode mode) {
        btnModeCamera.setAlpha(0.5f);
        btnModeMic.setAlpha(0.5f);
        btnModeVideo.setAlpha(0.5f);

        switch (mode) {
            case CAMERA:
                btnModeCamera.setAlpha(1.0f);
                btnMainAction.setImageResource(R.drawable.icons8_camera_30);
                tvInstruction.setText("Mode Kamera: Klik tombol untuk deteksi");
                break;
            case MIC:
                btnModeMic.setAlpha(1.0f);
                btnMainAction.setImageResource(R.drawable.icons8_microphone_24);
                tvInstruction.setText("Mode Suara: Klik tombol untuk bicara");
                break;
            case VIDEO:
                btnModeVideo.setAlpha(1.0f);
                btnMainAction.setImageResource(R.drawable.icons8_video_24);
                tvInstruction.setText("Mode Video: Klik tombol untuk upload video");
                break;
        }
        btnMainAction.clearColorFilter();
    }

    private void handleMainButtonAction() {
        switch (currentMode) {
            case CAMERA:
                if (isDetectingGestures) stopGestureDetection();
                else startGestureDetection();
                break;
            case MIC:
                // PANGGIL POPUP GOOGLE MIC
                promptSpeechInput();
                break;
            case VIDEO:
                videoPickerLauncher.launch("video/*");
                break;
        }
    }

    // --- FITUR MIC (GOOGLE POPUP METHOD) ---
    // Metode ini memanggil aktivitas Google Voice, bukan service background
    // Ini melewati batasan "Client Error" pada Android 11+

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Silakan bicara...");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception a) {
            Toast.makeText(getContext(), "Fitur Mic tidak didukung di HP ini", Toast.LENGTH_SHORT).show();
        }
    }

    // Menangkap hasil dari Popup Mic Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    tvTranslationResult.setText(result.get(0));
                }
            }
        }
    }

    // --- FITUR VIDEO PLAYER ---

    private void playVideo(Uri uri) {
        if (videoPreview == null) return;
        tvInstruction.setText("Memutar Video...");
        tvTranslationResult.setText("Menganalisis Video...");

        videoPreview.setVisibility(View.VISIBLE);
        videoPreview.setVideoURI(uri);

        videoPreview.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoPreview.start();
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    tvTranslationResult.setText("Video terdeteksi: \"Halo, Apa Kabar?\""), 2000);
        });
    }

    // --- FITUR KAMERA (DUMMY) ---

    private void startGestureDetection() {
        isDetectingGestures = true;
        tvInstruction.setText("Mendeteksi gerakan... (Klik stop)");
        tvTranslationResult.setText("...");
        btnMainAction.setColorFilter(ContextCompat.getColor(requireContext(), R.color.switch_green));
    }

    private void stopGestureDetection() {
        isDetectingGestures = false;
        tvInstruction.setText("Mode Kamera: Klik tombol untuk deteksi");
        btnMainAction.clearColorFilter();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                if (viewFinder != null) preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, image -> {
                    if (currentMode == AppMode.CAMERA && isDetectingGestures) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastUpdateTime >= 1500) {
                            String randomWord = dummyVocabulary[new Random().nextInt(dummyVocabulary.length)];
                            new Handler(Looper.getMainLooper()).post(() -> tvTranslationResult.setText(randomWord));
                            lastUpdateTime = currentTime;
                        }
                    }
                    image.close();
                });

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("SuaraTangan", "Gagal Camera", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) startSession();
            else Toast.makeText(requireContext(), "Izin diperlukan!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) cameraExecutor.shutdown();
    }
}