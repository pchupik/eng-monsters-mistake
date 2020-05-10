package ua.`in`.englishmonsters.mymistake

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> WelcomeFragment()
        1 -> GeneratorFragment()
        2 -> ShareFragment()
        3 -> WatchVideoFragment()
        else -> Fragment()
    }

    override fun getCount(): Int = 4
}