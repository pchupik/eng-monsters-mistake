package ua.`in`.englishmonsters.mymistake

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.common_bg.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShareActivity : AppCompatActivity() {


    var bitmapUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        bottom_text.setText(R.string.share)
        bottom_text.setOnClickListener{

            bitmapUri?.let { bitmapUri ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    setDataAndType(bitmapUri, "image/png")
                    putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivity(Intent(this, WiseActivity::class.java))
                startActivity(Intent.createChooser(intent, null))
            }
        }

        bottom_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_24dp,0,0,0)

        bitmapUri = intent.getParcelableExtra("bitmapUri")

        resultImageView.setImageURI(bitmapUri)
    }



}
