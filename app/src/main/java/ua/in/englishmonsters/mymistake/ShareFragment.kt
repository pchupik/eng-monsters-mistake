package ua.`in`.englishmonsters.mymistake

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_share.*
import java.io.IOException
import java.io.OutputStream
import java.util.*


class ShareFragment : Fragment() {

    private lateinit var viewModel: MistakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MistakeViewModel::class.java)
        viewModel.getCardUriData().observe(viewLifecycleOwner, Observer {
            resultImageView.setImageDrawable(null)
            resultImageView.setImageURI(it)
        })


        button_share.setOnClickListener{

            viewModel.getCardUriData().value?.let { bitmapUri ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    setDataAndType(bitmapUri, "image/png")
                    putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                (activity as? Parent)?.next()
                startActivity(Intent.createChooser(intent, null))
            }
        }

        button_store.setOnClickListener {
            saveImageCheckingPermission()
            (activity as? Parent)?.next()
        }

    }

    private fun saveImage(){
        viewModel.getCardUriData().value?.let { bitmapUri ->
            if (saveImage(bitmapUri, "my_mistake_${Date().time}"))
                Toast.makeText(requireContext(), "Збережено", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(), "Не вдалося зберегти", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(bitmapUri: Uri, name: String) : Boolean {
        var fos: OutputStream? = null
        try {
            val resolver: ContentResolver? = context?.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    // Add the date meta data to ensure the image is added at the front of the gallery
                    put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                }
             fos = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let {uri ->
                    resolver.openOutputStream(uri)
                }
            fos?.let {
                resolver?.openInputStream(bitmapUri)?.copyTo(it)
            }
        } catch (e: IOException){
            Log.e("share", "failed to save image", e)
            return false
        } finally {
            fos?.close()
        }
        return fos != null
    }

    private val MY_PERMISSIONS_REQUEST_STORAGE = 2
    private fun saveImageCheckingPermission(){
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_STORAGE)
        } else {
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    saveImage()
                } else {
                    // permission denied
                    Toast.makeText(
                        requireContext(),
                        "Немає дозволу для збереженя фото",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }
}
