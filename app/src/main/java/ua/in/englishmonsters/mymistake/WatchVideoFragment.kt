package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_video.*


class WatchVideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
