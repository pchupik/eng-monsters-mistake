package ua.`in`.englishmonsters.mymistake

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import glimpse.core.crop
import glimpse.core.findCenter
import kotlinx.android.synthetic.main.fragment_generator.*
import java.io.File
import java.io.IOException


class GeneratorFragment : Fragment() {

    private lateinit var viewModel: MistakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_generator, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MistakeViewModel::class.java)

        viewModel.getCardData().observe(viewLifecycleOwner, Observer {
            nameEditText.setText(it.name)
            if (it.photo != null)
                photoImageView.setImageBitmap(it.outlinedPhoto(context!!))
        })

        generate_button.setOnClickListener{
            viewModel.setName(nameEditText.text.toString())
            context?.let {
                viewModel.generate(it)
            }
            (activity as? Parent)?.next()
        }


        photoFrameImageView.setOnClickListener {
            choosePhoto()
        }

    }

    val MY_PERMISSIONS_REQUEST_CAMERA = 1
    private fun choosePhoto(){
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    // permission denied
                }
                return
            }
        }
    }

    fun openCamera() {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "ua.in.englishmonsters.mymistake.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }, 0, null)
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = File("${context!!.filesDir.path}/photos").also {
            if (!it.exists())
                it.mkdirs()
        }
        return  File(storageDir, "photo.jpg").apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val decodeFile = BitmapFactory.decodeFile(currentPhotoPath, null)

                val bitmap : Bitmap? = decodeFile ?: data.extras?.get("data") as? Bitmap

                val rotatedBitmap = rotateIfNeeded(bitmap) ?: bitmap

                val cropped = rotatedBitmap?.cropPhoto()

                val scaledBitmap = cropped?.let {
                    val matrix = Matrix()
                    matrix.postScale(1080f/it.width, 1080f/it.height)
                    Bitmap.createBitmap(
                        it,
                        0,
                        0,
                        it.width,
                        it.height,
                        matrix,
                        true
                    )
                }

                viewModel.setPhoto(scaledBitmap)
            }
        }
    }

    private fun rotateIfNeeded(bitmap: Bitmap?) : Bitmap? {
        val ei = ExifInterface(currentPhotoPath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap?.rotateImage(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap?.rotateImage(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> bitmap?.rotateImage(270f)
            else -> bitmap
        }
        return rotatedBitmap
    }

    private fun Bitmap.cropPhoto() : Bitmap{
        val bitmap = this
        val (x, y) = bitmap.findCenter()
        val min = kotlin.math.min(bitmap.width, bitmap.height)
        val cropped = bitmap.crop(x, y, min, min)
        return cropped
    }

}
