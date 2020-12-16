package com.lmx.rxjava2retrofit2.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lmx.rxjava2retrofit2.R
import kotlinx.android.synthetic.main.item_viewpager_txt.view.*

/**
 * Created by lmx on 2020/12/16
 * Describe:viewPagerAdapter
 */
class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val colors: MutableList<Int> = mutableListOf()

    init {
        colors.add(Color.parseColor("#3700B3"))
        colors.add(Color.parseColor("#3000EE"))
        colors.add(Color.parseColor("#03DAC6"))
        colors.add(Color.parseColor("#BA86FC"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager_txt, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            tvName?.text = "viewPager $position"
            tvName?.setTextColor(colors[position])
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)
}