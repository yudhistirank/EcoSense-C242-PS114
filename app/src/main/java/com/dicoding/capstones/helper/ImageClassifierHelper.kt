package com.dicoding.capstones.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.dicoding.capstones.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File

class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener
) {

    private var imageClassifier: ImageClassifier? = null
    private val inputWidth = 150
    private val inputHeight = 150

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val classifierOptions = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(0.5f)
            .setMaxResults(4)

        val baseOptions = BaseOptions.builder().setNumThreads(4)
        classifierOptions.setBaseOptions(baseOptions.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context, "model_with_metadata.tflite", classifierOptions.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageFile: File) {
        if (imageClassifier == null) setupImageClassifier()

        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)?.copy(Bitmap.Config.ARGB_8888, true)

        bitmap?.let {
            val tensorImage = preprocessImage(it)
            val results = imageClassifier?.classify(tensorImage)

            if (results != null) {
                classifierListener.onResult(results)
            } else {
                classifierListener.onError(context.getString(R.string.image_classifier_failed))
            }
        } ?: classifierListener.onError(context.getString(R.string.image_classifier_failed))
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputWidth, inputHeight, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        return imageProcessor.process(tensorImage)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(result: List<Classifications>)
    }
}
