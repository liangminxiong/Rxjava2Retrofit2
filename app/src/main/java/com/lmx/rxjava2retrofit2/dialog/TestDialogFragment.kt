package com.lmx.rxjava2retrofit2.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.lmx.rxjava2retrofit2.R
import kotlinx.android.synthetic.main.fragment_test_dialog.*


/**
 * Created by lmx on 2020/12/19
 * Describe: 测试
 */
class TestDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_dialog, container, false)
    }

//    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen) //dialog全屏
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000));
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        tvTest.setOnClickListener { dismiss() }
        tvTest.setBackgroundColor(Color.parseColor("#ffcc0000"))
    }


    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0.0f //Dialog外边框透明
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ) //高度自适应，宽度全屏
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        windowParams.gravity = Gravity.TOP //在顶部显示
//        windowParams.windowAnimations = R.style.TopDialogAnimation
//        window.attributes = windowParams
    }

    /**
     * 获取状态栏高度（单位：px）
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resources.getDimensionPixelSize(resourceId) == 0) 60 else resources.getDimensionPixelSize(
            resourceId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
    }

}