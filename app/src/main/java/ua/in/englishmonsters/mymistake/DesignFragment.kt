package ua.`in`.englishmonsters.mymistake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_design.*


class DesignFragment : Fragment() {

    private lateinit var viewModel: MistakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_design, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MistakeViewModel::class.java)
        viewModel.getCardUriData().observe(viewLifecycleOwner, Observer {
            resultImageView.setImageDrawable(null)
            resultImageView.setImageURI(it)
            result_decoration.visibility = View.VISIBLE
        })

        button_done.setOnClickListener {
            (activity as? Parent)?.next()
        }

        button_change_bg.setOnClickListener {
            viewModel.changeBg(context!!)
        }

        button_rotate.setOnClickListener {
            viewModel.rotate(context!!)
        }
    }
}
