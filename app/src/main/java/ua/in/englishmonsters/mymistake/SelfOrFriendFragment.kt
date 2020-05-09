package ua.`in`.englishmonsters.mymistake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_self_or_friend.*
import ua.`in`.englishmonsters.mymistake.Parent
import ua.`in`.englishmonsters.mymistake.R


class SelfOrFriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_self_or_friend, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for_me_button.setOnClickListener {
            (activity as? Parent)?.next()
        }
        for_friend_button.setOnClickListener {
            val params = Bundle()
            params.putBoolean("for_friend", true)
            (activity as? Parent)?.next(params)
        }
    }
}
