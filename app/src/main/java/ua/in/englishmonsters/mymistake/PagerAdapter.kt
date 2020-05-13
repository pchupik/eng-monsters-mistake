package ua.`in`.englishmonsters.mymistake

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = listOf(
        WelcomeFragment(),
        GeneratorFragment(),
        DesignFragment(),
        ShareFragment(),
        WatchVideoFragment()
    )

    override fun getItem(position: Int): Fragment = fragments.getOrElse(position){ Fragment() }

    override fun getCount(): Int = fragments.size
}