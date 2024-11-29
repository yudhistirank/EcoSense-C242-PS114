package com.dicoding.capstones.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstones.R
import com.dicoding.capstones.databinding.FragmentHomeBinding
import com.dicoding.capstones.helper.ImageClassifierHelper
import com.dicoding.capstones.viewModel.MainViewModel
import com.dicoding.capstones.viewModel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.NumberFormat
import java.util.*

private const val MAXIMAL_SIZE = 1000000

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var mainViewModel: MainViewModel

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            mainViewModel.saveUri(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[MainViewModel::class.java]

        binding = FragmentHomeBinding.bind(view)

        mainViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.previewImageView.setImageURI(uri)
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun showImage() {
        mainViewModel.currentImageUri.observe(viewLifecycleOwner) {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        mainViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            val imageFile =
                uri?.let { uriToFile(it, requireContext())?.reduceFileImage() }
            Log.d("Image Classification File", "Show Image: ${imageFile?.path}")

            imageClassifierHelper = ImageClassifierHelper(context = requireContext(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResult(result: List<Classifications>) {
                        if (uri != null) {
                            mainViewModel.insertHistory(uri, result)
                        }
                        result?.let { it ->
                            if (it.isNotEmpty()) {
                                val category = it[0].categories[0]
                                val displayResult =
                                    "${category.label} " + NumberFormat.getPercentInstance()
                                        .format(category.score).trim()
                                if (uri != null) {
                                    moveToResult(uri, displayResult)
                                }
                            } else {
                                showToast("No classifications found")
                            }
                        }
                    }
                })

            if (imageFile != null) {
                imageClassifierHelper.classifyStaticImage(imageFile)
            } else {
                Log.e("AnalyzeImage", "Image file is null, cannot analyze.")
                showToast("Image file is null, cannot analyze.")
            }
        } ?: Log.e("AnalyzeImage", "Current Image URI is null")
    }

    private fun moveToResult(uri: Uri, result: String) {
        val resultFragment = ResultFragment().apply {
            arguments = Bundle().apply {
                putString(ResultFragment.EXTRA_IMAGE_URI, uri.toString())
                putString(ResultFragment.EXTRA_RESULT, result)
            }
        }

        // Ganti fragmen menggunakan FragmentTransaction
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, resultFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun File.reduceFileImage(): File {
        val bitmap = BitmapFactory.decodeFile(this.path)
        var compressQuality = 100
        var streamLength: Int
        var byteArray: ByteArray // Declare byteArray here

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            byteArray = bmpStream.toByteArray() // Initialize byteArray
            streamLength = byteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)

        val outputFile = File.createTempFile("compressed_", ".jpg", requireContext().cacheDir)
        FileOutputStream(outputFile).use { it.write(byteArray) }
        return outputFile
    }


    private fun uriToFile(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, System.currentTimeMillis().toString())
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
        }
        return file
    }
}
