package com.example.examenandroidalbertobarcenas.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.examenandroidalbertobarcenas.fragments.GaleryFragment
import com.example.examenandroidalbertobarcenas.fragments.MapsFragment
import com.example.examenandroidalbertobarcenas.fragments.StartFragment

class PagerAdapter(fragmentManager: FragmentManager, internal var totalTabs: Int) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StartFragment()
            1 -> MapsFragment()
            2 -> GaleryFragment()
            else -> StartFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}