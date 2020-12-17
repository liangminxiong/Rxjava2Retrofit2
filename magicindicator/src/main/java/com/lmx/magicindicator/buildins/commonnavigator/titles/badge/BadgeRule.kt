package com.lmx.magicindicator.buildins.commonnavigator.titles.badge

/**
 * Created by lmx on 2020/12/16
 * Describe:角标的定位规则
 */
class BadgeRule(private var anchor: BadgeAnchor?, private var offset: Int = 0) {

    fun getAnchor(): BadgeAnchor? {
        return anchor
    }

    fun setAnchor(anchor: BadgeAnchor?) {
        this.anchor = anchor
    }

    fun getOffset(): Int {
        return offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
    }
}