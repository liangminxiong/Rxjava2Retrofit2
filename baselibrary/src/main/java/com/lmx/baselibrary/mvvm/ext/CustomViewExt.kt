package com.lmx.baselibrary.mvvm.ext

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by lmx on 2020/5/7
 * Describe 项目中自定义类的拓展函数--很香
 */

//绑定普通的Recyclerview
fun RecyclerView.init(
        layoutManger: RecyclerView.LayoutManager,
        bindAdapter: RecyclerView.Adapter<*>,
        isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

fun ViewPager2.init(
        fragment: Fragment,
        fragments: ArrayList<Fragment>,
        isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}
