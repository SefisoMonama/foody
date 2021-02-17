package com.strixtechnology.foody.adapters

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private var resultBundle: Bundle,
    private var fragments: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}