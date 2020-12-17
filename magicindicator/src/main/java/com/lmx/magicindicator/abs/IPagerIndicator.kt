package com.lmx.magicindicator.abs

import com.lmx.magicindicator.buildins.PositionData

/**
 * Created by lmx on 2020/12/16
 * Describe:抽象的viewpager指示器，适用于CommonNavigator
 */
interface IPagerIndicator {

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

    fun onPageSelected(position: Int)

    fun onPageScrollStateChanged(state: Int)

    fun onPositionDataProvide(dataList: MutableList<PositionData>)
}