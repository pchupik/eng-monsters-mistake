package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wise.*


class WiseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wise)

        toVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ELbxfz5xJVE"))
            startActivity(intent)
        }

//        bottom_text.setText(R.string.share)
//        bottom_text.setOnClickListener{
//
//                val intent = Intent(Intent.ACTION_SEND).apply {
//                    setType("text/plain");
//                    putExtra(Intent.EXTRA_SUBJECT, "прємудрасті");
//                    putExtra(Intent.EXTRA_TEXT, "https://youtu.be/ELbxfz5xJVE");
//                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                }
//                startActivity(Intent.createChooser(intent, null))
//
//        }
//
//        bottom_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_24dp,0,0,0)
    }
}
