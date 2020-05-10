package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_share.*

class ShareFragment : Fragment() {

    private lateinit var viewModel: MistakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_share, container, false)
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

        resultImageView.setOnClickListener {
            viewModel.changeBg(context!!)
        }

    }
}
