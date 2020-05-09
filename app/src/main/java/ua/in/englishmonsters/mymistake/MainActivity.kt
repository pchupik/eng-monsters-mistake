package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_self_or_friend.*

class MainActivity : AppCompatActivity(), Parent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = PagerAdapter(supportFragmentManager)



    }

    override fun next(params: Bundle?) {

        view_pager.setCurrentItem(view_pager.currentItem + 1, true)

        if (params != null) {
            val fragment = (view_pager.adapter as PagerAdapter).instantiateItem(
                view_pager,
                view_pager.currentItem
            )
            (fragment as Fragment).arguments = params
        }
    }
}
