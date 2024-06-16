package com.rohman.nutriscanapp.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.rohman.nutriscanapp.createCustomTempFile
import com.rohman.nutriscanapp.data.api.ApiConfig
import com.rohman.nutriscanapp.data.api.ApiResponse
import com.rohman.nutriscanapp.data.api.PredictionData
import com.rohman.nutriscanapp.databinding.ActivityCameraBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.captureImage.setOnClickListener { takePhoto() }
        binding.insertImage.setOnClickListener { startGallery() }

        if (!isCameraPermissionGranted()) {
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Izin kamera diberikan. Anda sekarang dapat menggunakan kamera.")
            } else {
                showToast("Izin kamera ditolak. Aplikasi mungkin tidak berfungsi dengan baik.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImage(imageUri: Uri) {
        showLoad(true)
        val jsonObject = uriToJson(imageUri, this)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val apiService = ApiConfig.getApiService()
        val call = apiService.predict(requestBody)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                showLoad(false)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        displayResults(apiResponse.data, imageUri)
                    } else {
                        showToast("No data received")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                    showToast(errorResponse.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                showLoad(false)
                showToast("An error occurred: ${t.message}")
                Log.e("uploadImage", "Error: ${t.message}", t)
            }
        })
    }

    private fun uriToJson(imageUri: Uri, context: Context): JSONObject {
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        inputStream.close()

        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)

        val jsonObject = JSONObject()
        jsonObject.put("image", base64Image)
        return jsonObject
    }

    private fun displayResults(data: List<PredictionData>, imageUri: Uri) {
        if (data.isNotEmpty()) {
            val prediction = data[0]
            val intent = Intent(this, ResultCameraActivity::class.java)
            val jsonData = Gson().toJson(data)
            intent.putExtra(ResultCameraActivity.EXTRA_PREDICT_RESULT, jsonData)
            intent.putExtra(EXTRA_CAMERAX_IMAGE, imageUri.toString())
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this@CameraActivity, "Failed to open camera.", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "startCamera: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        showLoad(true)
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    uploadImage(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    showLoad(false)
                    Toast.makeText(
                        this@CameraActivity,
                        "Failed to capture image.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}", exc)
                }
            }
        )
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            uploadImage(selectedImg)
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val EXTRA_PREDICT_RESULT = "predict_result"
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
}