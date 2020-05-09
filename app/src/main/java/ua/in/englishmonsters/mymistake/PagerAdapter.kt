package ua.`in`.englishmonsters.mymistake

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> WelcomeFragment()
        1 -> SelfOrFriendFragment()
        2 -> GeneratorFragment()
        3 -> ShareFragment()
        4 -> WiseActivity()
        else -> Fragment()
    }

    override fun getCount(): Int = 5
}