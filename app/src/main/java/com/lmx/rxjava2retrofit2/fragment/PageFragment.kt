package com.lmx.rxjava2retrofit2.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var mPosition: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPosition = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvContent.text = "PageFragment $mPosition"
    }
}