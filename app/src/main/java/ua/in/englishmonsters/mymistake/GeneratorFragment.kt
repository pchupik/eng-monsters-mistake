package ua.`in`.englishmonsters.mymistake

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_generator.*


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
            photoImageView.setClipToOutline(true)
            if (it.photo != null)
            photoImageView.setImageBitmap(it.photo)
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

            photoImageView.setClipToOutline(true)
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
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            var selectedImage: Uri? = null

            if (data != null) {
//                selectedImage = data.data
//                photoImageView.setImageURI(selectedImage)
                val bitmap : Bitmap? = data.extras?.get("data") as? Bitmap

                val scaledBitmap = bitmap?.let {
                    val matrix = Matrix()
                    matrix.postScale(1080f/it.width, 1080f/it.height)
                    Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        true
                    )
                }


//                photoImageView.setImageBitmap(scaledBitmap)
//                photo = scaledBitmap
                viewModel.setPhoto(scaledBitmap)
            }
        }
    }
}
