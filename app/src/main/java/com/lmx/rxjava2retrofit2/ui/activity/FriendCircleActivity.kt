package com.lmx.rxjava2retrofit2.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.lmx.rxjava2retrofit2.R
import com.lmx.rxjava2retrofit2.bold
import com.lmx.rxjava2retrofit2.text
import kotlinx.android.synthetic.main.activity_friend_circle.*

/**
 * Created by lmx on 2020/12/22
 * Describe: 朋友圈
 */
class FriendCircleActivity : BaseActivity() {

    companion object {

        private const val KEY_TYPE = "type"
        fun newInstance(context: AppCompatActivity, type: Int) {
            val intent = Intent()
            intent.apply {
                setClass(context, FriendCircleActivity::class.java)
                putExtra(KEY_TYPE, type)
            }
            context.startActivity(intent)
        }
    }

    private val type: Int by lazy { intent.getIntExtra(KEY_TYPE, -1) }

    override fun layoutId(): Int = R.layout.activity_friend_circle

    override fun initView() {
        tvTitle.text("朋友圈")
        tvTitle.bold(true)
    }
}