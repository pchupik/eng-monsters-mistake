package ua.`in`.englishmonsters.mymistake

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import glimpse.core.Glimpse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Parent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = PagerAdapter(supportFragmentManager)

        Glimpse.init(application)

        val viewModel = ViewModelProvider(this).get(MistakeViewModel::class.java)
        viewModel.getCardData().observe(this, Observer {
            val pagerAdapter = view_pager.adapter as? PagerAdapter
            pagerAdapter?.showDesign = it.canGenerate()
            pagerAdapter?.notifyDataSetChanged()
        })

        view_pager.addOnPageChangeListener( PageChangeListener { position, positionOffset ->
            position.indicatorView()?.alpha = (1f - positionOffset).toAlpha()
            (position + 1).indicatorView()?.alpha = positionOffset.toAlpha()

            if (position == 0) {
                val welcomeFragment = (view_pager.adapter as? PagerAdapter)?.getItem(0)
                (welcomeFragment as? WelcomeFragment)?.drop(positionOffset)
            }
        })
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

    class PageChangeListener(val onScrolled: (position: Int, positionOffset: Float) -> Unit ) : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            onScrolled(position, positionOffset)
        }

        override fun onPageSelected(position: Int) {
        }
    }

    private fun Int.indicatorView() : ImageView? = when (this){
        0 -> indicator_1
        1 -> indicator_2
        2 -> indicator_3
        3 -> indicator_4
        4 -> indicator_5
        else -> null
    }

    private fun Float.toAlpha() : Float = 0.6f + this * 0.4f

}
