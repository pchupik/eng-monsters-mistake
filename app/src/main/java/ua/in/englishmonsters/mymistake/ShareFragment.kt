package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_share.*

class ShareFragment : Fragment() {


    var bitmapUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_share, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_share.setText(R.string.share)
        button_share.setOnClickListener{

            bitmapUri?.let { bitmapUri ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    setDataAndType(bitmapUri, "image/png")
                    putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                (activity as? Parent)?.next()
                startActivity(Intent.createChooser(intent, null))
            }
        }

//        button_share.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share_24dp,0,0,0)

        bitmapUri = arguments?.getParcelable("bitmapUri")

        resultImageView.setImageURI(bitmapUri)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            bitmapUri = arguments?.getParcelable("bitmapUri")

            resultImageView.setImageURI(bitmapUri)
        }
    }
}
