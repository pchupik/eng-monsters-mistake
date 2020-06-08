package ua.`in`.englishmonsters.mymistake

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CardData {

    var name: String = ""
    @DrawableRes
    var bgRes: Int = bgs.random()
    var photo: Bitmap? = null

    companion object {
        val bgs = listOf(
            R.drawable.card_bg_98,
            R.drawable.card_bg_102,
            R.drawable.card_bg_103,
            R.drawable.card_bg_104,
            R.drawable.card_bg_105,
            R.drawable.card_bg_106,
            R.drawable.card_bg_107
        )
    }

    fun nextBgRes(): Int {
        return bgs[(bgs.indexOf(bgRes) + 1).let {
            if (it < bgs.size) it
            else 0
        }]


    }

    fun canGenerate() : Boolean = !(name.isEmpty() && photo == null)

    fun generate(context: Context) : Uri? {
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.monsters_font)
        val bitmap = createCard(name, context)
        val bitmapUri = bitmap?.store("right.png", context)
        return bitmapUri
    }

    val textPaint = TextPaint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    fun textPaintWithSize(size: Float) = TextPaint(textPaint).apply {
        textSize = size
    }

    fun createCard(name: String, context: Context) : Bitmap {

        val bmp = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val bg = context!!.getDrawable(bgRes)?.toBitmap(1080, 1080)!!
        canvas.drawBitmap(bg, 0, 0, 1080, 1080)

        if (photo == null)
            photo = context!!.getDrawable(R.drawable.ic_ellipse)?.toBitmap(1080, 1080)!!
        photo?.let {
            val bitmapShader = BitmapShader(
                it,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
            )
            val paint = Paint()
            paint.setShader(bitmapShader)

            val shape = context!!.getDrawable(R.drawable.ic_ellipse)?.toBitmap(1080, 1080)!!.extractAlpha()
            canvas.drawBitmap(shape, 300, 300, 480, 480, paint)
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

    inline fun Canvas.drawBitmap(bitmap: Bitmap, x: Int, y: Int, w: Int, h: Int, paint: Paint? = null){
        val photoRect = Rect(x, y, x + w, y + h)
        drawBitmap(bitmap,null, photoRect, paint)
    }


    private fun Bitmap.store(filename: String, context: Context, dir: File = targetDirectory(context)): Uri? {
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

    private fun targetDirectory(context: Context) : File {
        return File("${context!!.filesDir.path}/generated").also {
            if (!it.exists())
                it.mkdirs()
        }
    }

    private fun Bitmap.outlinePhoto(context: Context) : Bitmap {
        val bmp = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val bitmapShader = BitmapShader(this, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        val paint = Paint()
        paint.setShader(bitmapShader)
        val shape = context!!.getDrawable(R.drawable.ic_ellipse)?.toBitmap(1080, 1080)!!.extractAlpha()
//        canvas.drawBitmap(shape, 0f,0f, paint)
        canvas.drawBitmap(shape, 40, 40, 1000, 1000, paint)
        return bmp
    }

    fun outlinedPhoto(context: Context) : Bitmap? {
        return photo?.outlinePhoto(context)
    }

    fun rotatePhoto(){
        photo = photo?.rotateImage(90f)
    }


}

public fun Bitmap.rotateImage(angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        this, 0, 0, width, height,
        matrix, true
    )
}