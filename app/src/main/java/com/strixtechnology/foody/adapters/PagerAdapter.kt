package com.strixtechnology.foody.adapters

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    private var resultBundle: Bundle,
    private var fragments: ArrayList<Fragment>,
    private var title: ArrayList<String>,
    private var fm :FragmentManager
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}