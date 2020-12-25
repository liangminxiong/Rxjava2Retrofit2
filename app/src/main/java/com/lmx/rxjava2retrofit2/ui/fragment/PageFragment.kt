package com.lmx.rxjava2retrofit2.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lmx.rxjava2retrofit2.R
import kotlinx.android.synthetic.main.fragment_page.*

/**
 * Created by lmx on 2020/12/16
 * Describe:PageFragment
 */
class PageFragment : Fragment() {

    companion object {

        private const val POSITION: String = "POSITION"
        fun newInstance(position: Int): PageFragment? {
            val args = Bundle()
            args.putInt(POSITION, position)
            val fragment = PageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    private var mPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPosition = it.getInt(POSITION)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        for (i in 0 until mPosition) {
            wrapLayout.addView(getTextView(i))
        }
    }

    private fun getTextView(position: Int): TextView? {
        val tv = TextView(activity)
        tv.text = String.format("${getTxt("人生不如意事常八九。婚姻就是爱情的坟墓", position)} %s", position)
        tv.setBackgroundResource(R.drawable.wraplayout_tv_bg)
        tv.setPadding(30, 10, 30, 10)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        tv.setTextColor(-0x919192)
        return tv
    }

    private fun getTxt(txt: String, position: Int): String {
        val length = txt.length
        val i = mPosition - position
        mPosition = if (i <= 0) 2 else i
        if (mPosition > length) {
            mPosition = length
        }
        return txt.substring(0, mPosition)
    }

}