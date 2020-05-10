package ua.`in`.englishmonsters.mymistake

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_generator.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random


class GeneratorFragment : Fragment() {

    private lateinit var viewModel: MistakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_generator, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MistakeViewModel::class.java)

        viewModel.getCardData().observe(viewLifecycleOwner, Observer {
            nameEditText.setText(it.name)
            photoImageView.setClipToOutline(true)
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
    fun choosePhoto(){
//        Toast.makeText(this, "тут має вилізти камера або чузер, але покищо буде просто фото Олі", Toast.LENGTH_LONG).show()
//        photoImageView.setImageResource(R.drawable.img)

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
                // TODO
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            openCamera()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openCamera()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
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
