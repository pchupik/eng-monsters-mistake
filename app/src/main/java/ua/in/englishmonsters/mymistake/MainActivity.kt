package ua.`in`.englishmonsters.mymistake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
