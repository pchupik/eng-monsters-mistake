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
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_generator.*
import kotlinx.android.synthetic.main.common_bg.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GeneratorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        bottom_text.setText(R.string.generate)
        bottom_text.setOnClickListener{

            val bitmap = createCard(nameEditText.text.toString(), authorEditText.text.toString())//getDrawable(R.drawable.lucky_o)?.toBitmap(1080, 1080)
            val bitmapUri = bitmap?.store("right.png")
            val intent = Intent(this, ShareActivity::class.java)
            intent.putExtra("bitmapUri", bitmapUri)
            startActivity(intent)
        }

        if (intent.getBooleanExtra("for_friend", false)){
            bottom_text.setText("Відправити")
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



        textPaint.typeface = ResourcesCompat.getFont(this, R.font.monsters_font)
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
        canvas.drawColor(getColor(R.color.colorPrimary)) // 0xF0582E
//        val photo = getDrawable(R.drawable.img)?.toBitmap(322, 427)!!
        photo?.let {
            canvas.drawBitmap(it, 660, 165, 322, 427)
        }

        val stamp= getDrawable(R.drawable.stamp)?.toBitmap(315, 305)!!
        canvas.drawBitmap(stamp, 783, 440, 315,305)

        canvas.drawText(name, 96f, 863f, textPaintWithSize(150f))
        canvas.drawText("Власник", 96f, 887f + 49f, textPaintWithSize(50f).also { it.alpha = 0x80 })

        canvas.drawText("Персональне", 96f, 248f + 68f, textPaintWithSize(70f))
        canvas.drawText("Право на", 96f, 315f + 194f / 2f, textPaintWithSize(100f))
        canvas.drawText("помилку", 96f, 315f + 194f, textPaintWithSize(100f))

        val staticLayout = StaticLayout.Builder.obtain(
            from,
            0,
            from.length,
            textPaintWithSize(50f),
            canvas.width - 80
        )
            .setAlignment(Layout.Alignment.ALIGN_OPPOSITE)
            .build()
        canvas.translate(0f, 983f)
        staticLayout.draw(canvas)


//        canvas.drawText(from, 612f, 983f + 49f, textPaintWithSize(50f))

        return bmp

    }

    inline fun Canvas.drawBitmap(bitmap: Bitmap, x: Int, y: Int, w: Int, h: Int){
        val photoRect = Rect(x, y, x + w, y + h)
        drawBitmap(bitmap,null, photoRect, null)
    }


    private fun Bitmap.store(filename: String, dir: File = targetDirectory(), context: Context = this@GeneratorActivity): Uri? {
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
        return File("${filesDir.path}/generated").also {
            if (!it.exists())
                it.mkdirs()
        }
    }

    val MY_PERMISSIONS_REQUEST_CAMERA = 1
    fun choosePhoto(){
//        Toast.makeText(this, "тут має вилізти камера або чузер, але покищо буде просто фото Олі", Toast.LENGTH_LONG).show()
//        photoImageView.setImageResource(R.drawable.img)

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
                // TODO
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
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

                val rotatedBitmap = bitmap?.let {
                    val matrix = Matrix()
                    matrix.postRotate(-90f)
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


                photoImageView.setImageBitmap(rotatedBitmap)
                photo = rotatedBitmap
            }
        }
    }
}
