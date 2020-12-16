package com.lmx.rxjava2retrofit2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by lmx on 2020/12/16
 * Describe:ViewPagerFragmentStateAdapter
 */
class ViewPagerFragmentStateAdapter(
    activity: FragmentActivity,
    private var fragments: MutableList<Fragment>
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
