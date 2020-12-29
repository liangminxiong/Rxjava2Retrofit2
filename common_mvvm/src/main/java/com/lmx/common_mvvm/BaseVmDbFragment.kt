package com.lmx.common_mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lmx.common_mvvm.ext.getVmClazz

/**
 * Created by lmx on 2020/5/7
 * Describe : ViewModelFragment基类，自动把ViewModel注入Fragment和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM
    //是否初始化
    fun checkViewModelInit(): Boolean {
        if (this::mViewModel.isInitialized) {//重要，this::前缀是必须的。
            //如果已经初始化了，返回
            return true
        }
        return false
    }

    lateinit var mDatabind: DB

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mDatabind = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDatabind.lifecycleOwner = this
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = createViewModel()
        initView(savedInstanceState)
        onVisible()
        registorDefUIChange()
        initData()
        onViewClicked()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
    }

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 绑定该视图的点击事件 需要给view设置普通的点击事件时可在fragment中重写使用 例子
     *   override fun onViewClicked() {
    setOnclick(listOf(viewId1,viewId2)) {
    when (it.id) {
    R.id.viewId1 -> {

    }
    R.id.viewId2 -> {

    }
    }
    }
    }
     */
    open fun onViewClicked() {}

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
            createObserver()
        }
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.uiChange.showDialog.observe(viewLifecycleOwner, Observer {
            showLoading()
        })
        mViewModel.uiChange.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoading()
        })
    }
}