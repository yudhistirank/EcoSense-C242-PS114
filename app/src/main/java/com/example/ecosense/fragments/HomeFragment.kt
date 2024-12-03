package com.example.ecosense.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private var imageUri: Uri? = null

    private val apiService: ApiService by lazy { ApiClient.apiService }

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

        // Set up button to show classification result
        binding.btnResult.setOnClickListener {
            imageUri?.let {
                classifyWaste(it)
            } ?: Toast.makeText(requireContext(), "Please upload an image first", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            // Use FileProvider to get a secure URI for camera image
            val photoUri = createImageUri()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == android.app.Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    imageUri = data?.data
                    imageUri?.let {
                        binding.imageViewResult.setImageURI(it)
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    imageUri?.let {
                        binding.imageViewResult.setImageURI(it)
                    }
                }
            }
        }
    }

    private fun createImageUri(): Uri {
        val imageFile = File(requireContext().cacheDir, "camera_image.jpg")
        return Uri.fromFile(imageFile)
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
                    if (predictionResponse != null) {
                        // Access the result from the data object
                        val result = predictionResponse.data.result
                        if (!result.isNullOrEmpty()) {
                            binding.tvResultText.text = result
                            binding.tvResultText.visibility = View.VISIBLE
                        } else {
                            binding.tvResultText.text = "No result available"
                            binding.tvResultText.visibility = View.VISIBLE
                        }
                    } else {
                        Log.e("API Response Error", "Empty response body")
                        binding.tvResultText.text = "Error: No data received"
                        binding.tvResultText.visibility = View.VISIBLE
                    }
                } else {
                    Log.e("API Response Error", "Error code: ${response.code()}, message: ${response.message()}")
                    Toast.makeText(requireContext(), "Failed to classify waste", Toast.LENGTH_SHORT).show()
                    binding.tvResultText.text = "Error: ${response.message()}"
                    binding.tvResultText.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Log.e("API Request Failure", t.message.orEmpty())
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                binding.tvResultText.text = "Error: ${t.message}"
                binding.tvResultText.visibility = View.VISIBLE
            }
        })
    }

    private fun getFileFromUri(uri: Uri): File? {
        val filePath = getRealPathFromURI(uri)
        return filePath?.let { File(it) }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
