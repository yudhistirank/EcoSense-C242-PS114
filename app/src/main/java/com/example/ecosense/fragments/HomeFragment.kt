package com.example.ecosense.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.ecosense.databinding.FragmentHomeBinding
import com.example.ecosense.R
import com.example.ecosense.network.ApiClient
import com.example.ecosense.network.ApiService
import com.example.ecosense.models.PredictionResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val apiService: ApiService by lazy { ApiClient.apiService }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri?.let { uri ->
                val jpgFile = convertToJpg(uri)
                if (jpgFile != null) {
                    imageUri = Uri.fromFile(jpgFile)
                    binding.imageViewResult.setImageURI(imageUri)
                } else {
                    Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.imageViewResult.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.layoutButtons.findViewById<View>(R.id.camera_button).setOnClickListener {
            openCamera()
        }

        binding.layoutButtons.findViewById<View>(R.id.gallery_button).setOnClickListener {
            openGallery()
        }

        binding.btnResult.setOnClickListener {
            imageUri?.let {
                classifyWaste(it)
            } ?: Toast.makeText(requireContext(), "Please upload an image first", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun openCamera() {
        // Hapus file lama jika ada
        imageUri?.let { uri ->
            val file = File(uri.path ?: "")
            if (file.exists()) file.delete()
        }

        // Buat file baru untuk gambar dari kamera
        val imageFile = File(requireContext().cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
        cameraLauncher.launch(imageUri)
    }


    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun classifyWaste(imageUri: Uri) {
        val file = getFileFromUri(imageUri)
        if (file == null) {
            Toast.makeText(requireContext(), "Invalid file", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        apiService.predictWaste(body).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    val result = predictionResponse?.data?.result
                    binding.tvResultText.text = result ?: "No result available"
                    binding.tvResultText.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Failed to classify waste", Toast.LENGTH_SHORT).show()
                    Log.e("API Response Error", "Error code: ${response.code()}, message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API Request Failure", t.message.orEmpty())
            }
        })
    }

    private fun convertToJpg(uri: Uri): File? {
        try {
            val bitmap = requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
            val outputFile = File(requireContext().cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 85, outputFile.outputStream())
            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    private fun getFileFromUri(uri: Uri): File? {
        val fileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r") ?: return null
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        fileDescriptor.close()
        return file
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
