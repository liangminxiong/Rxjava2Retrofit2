package com.lmx.magicindicator.abs

/**
 * Created by lmx on 2020/12/16
 * Describe:可测量内容区域的指示器标题
 */
interface IMeasurablePagerTitleView: IPagerTitleView {

    fun getContentLeft(): Int

    fun getContentTop(): Int

    fun getContentRight(): Int

    fun getContentBottom(): Int
}