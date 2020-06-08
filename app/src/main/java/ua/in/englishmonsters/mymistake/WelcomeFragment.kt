package ua.`in`.englishmonsters.mymistake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_welcome.*


class WelcomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        start_button.setOnClickListener {
            (activity as? Parent)?.next()
        }
    }

    fun drop(offset: Float) {
        imageViewFallen.translationX = imageViewFallen.width * offset
        imageViewFallen.translationY = imageViewFallen.height * 5 * offset
        imageViewFallen.rotation = 360 * offset
    }
}
