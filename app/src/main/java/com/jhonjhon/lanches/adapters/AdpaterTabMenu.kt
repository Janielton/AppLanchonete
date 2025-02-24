package com.jhonjhon.lanches.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.jhonjhon.lanches.tabs.TabBebidas
import com.jhonjhon.lanches.tabs.TabLanches
import com.jhonjhon.lanches.tabs.TabPersonalizado

class AdpaterTabMenu(
    fm: FragmentManager,
    behavior: Int,
    private val tabsNumber: Int) : FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TabLanches()
            1 -> TabBebidas()
            2 -> TabPersonalizado()
            else -> TabLanches()
        }
    }

    override fun getCount(): Int {
        return tabsNumber
    }

}