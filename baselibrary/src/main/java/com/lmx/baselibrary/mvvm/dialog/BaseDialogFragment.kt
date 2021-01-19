package com.lmx.baselibrary.mvvm.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


abstract class BaseDialogFragment : androidx.fragment.app.DialogFragment() {

    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        initEvent()

        initData()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window.attributes = params
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun isCanceledOnTouchOutside(outSide: Boolean) {
        dialog!!.setCanceledOnTouchOutside(outSide)//设置点击弹窗外面是否取消
    }

    abstract fun initData()

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initEvent()

    fun setOnclick(vararg views: View) {
        views.forEach {
            it.setOnClickListener(mOnCLickListener)
        }
    }

    private val mOnCLickListener = View.OnClickListener {
        onViewClick(it)
    }

    open fun onViewClick(view: View) {

    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (isAdded || isRemoving) -1 else super.show(
            transaction,
            tag + System.currentTimeMillis()
        )
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (isAdded || isRemoving) return
        super.showNow(manager, tag + System.currentTimeMillis())
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded || isRemoving) return
        val mClass = DialogFragment::class.java
        val dismissed = mClass.getDeclaredField("mDismissed")
        dismissed.isAccessible = true
        dismissed.set(this, false)

        val shownByMe = mClass.getDeclaredField("mShownByMe")
        shownByMe.isAccessible = true
        shownByMe.set(this, true)
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = context?.let {
                LoadingDialog(it, true)
            }
        }
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
        }
    }

    fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
        }
    }

}