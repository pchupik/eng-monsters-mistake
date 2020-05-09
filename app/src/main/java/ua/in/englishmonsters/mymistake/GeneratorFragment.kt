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
import kotlinx.android.synthetic.main.activity_generator.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random


class GeneratorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_generator, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        generate_button.setOnClickListener{

            val bitmap = createCard(nameEditText.text.toString(), authorEditText.text.toString())//getDrawable(R.drawable.lucky_o)?.toBitmap(1080, 1080)
            val bitmapUri = bitmap?.store("right.png", context!!)
//            val intent = Intent(this, ShareActivity::class.java)
//            intent.putExtra("bitmapUri", bitmapUri)
//            startActivity(intent)
            val params = Bundle().apply { putParcelable("bitmapUri", bitmapUri) }
            (activity as? Parent)?.next(params)
        }

        if (arguments?.getBoolean("for_friend", false) == true){
            authorEditText.visibility = VISIBLE
        } else {
            authorEditText.visibility = GONE
        }

        photoFrameImageView.setOnClickListener {
            choosePhoto()


            photoImageView.setClipToOutline(true)
        }

        authorEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && authorEditText.text.isEmpty()) {
                authorEditText.setText("від ")
                authorEditText.setSelection(4)
            }
        }



        textPaint.typeface = ResourcesCompat.getFont(context!!, R.font.monsters_font)
    }


    val textPaint = TextPaint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    fun textPaintWithSize(size: Float) = TextPaint(textPaint).apply {
        textSize = size
    }

    var photo : Bitmap? = null
    fun createCard(name: String, from: String) : Bitmap {

        val bmp = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        @DrawableRes val bgRes = if (Random.nextBoolean())
            R.drawable.card_bg_1
        else
            R.drawable.card_bg_3
        val bg = context!!.getDrawable(bgRes)?.toBitmap(1080, 1080)!!
        canvas.drawBitmap(bg, 0, 0, 1080, 1080)

        if (photo == null)
            photo = context!!.getDrawable(R.drawable.img)?.toBitmap(1080, 1080)!!
        photo?.let {
//            canvas.drawBitmap(it,  300, 300, 480, 480)

            val bitmapShader = BitmapShader(
                it,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
            val paint = Paint()
            paint.setShader(bitmapShader)
//            canvas.drawOval(300f,300f, 800f, 740f, paint)
            val path = Path()
            path.moveTo(300f, 300f)
            path.rLineTo(400f, 20f)
            path.rLineTo(80f, 400f)
            path.rLineTo(0f, 40f)
            path.rLineTo(-400f, 20f)
            path.rLineTo(-60f, -10f)
            path.close()

            canvas.drawPath(path, paint)
        }

        canvas.drawText("Право на помилку", 120f, 1000f, textPaintWithSize(100f))

        val staticLayout = StaticLayout.Builder.obtain(
            "$name має",
            0,
            "$name має".length,
            textPaintWithSize(100f),
            canvas.width
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()
        canvas.translate(0f, 800f)
        staticLayout.draw(canvas)

        return bmp

    }

    inline fun Canvas.drawBitmap(bitmap: Bitmap, x: Int, y: Int, w: Int, h: Int){
        val photoRect = Rect(x, y, x + w, y + h)
        drawBitmap(bitmap,null, photoRect, null)
    }


    private fun Bitmap.store(filename: String, context: Context, dir: File = targetDirectory()): Uri? {
        var bmpUri: Uri? = null
        var out: FileOutputStream? = null
        try {
            val file = File(dir, filename)
            out = FileOutputStream(file)
            compress(Bitmap.CompressFormat.PNG, 90, out)
            bmpUri = getFileProviderUri(context, file)

        } catch (e: IOException) {
            Log.e("ChartImage", "failed to store Bitmap", e)
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            Log.e("ChartImage", "failed to store Bitmap", e)
            e.printStackTrace()
        } finally {
            out?.close()
        }
        return bmpUri
    }

    fun getFileProviderUri(
        context: Context,
        file: File?
    ): Uri? {
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file!!
        )
    }

    private fun targetDirectory() : File {
        return File("${context!!.filesDir.path}/generated").also {
            if (!it.exists())
                it.mkdirs()
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


                photoImageView.setImageBitmap(scaledBitmap)
                photo = scaledBitmap
            }
        }
    }
}
