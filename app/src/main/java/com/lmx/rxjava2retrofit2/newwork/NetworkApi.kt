package com.lmx.rxjava2retrofit2.newwork

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.lmx.baselibrary.LenientGsonConverterFactory
import com.lmx.baselibrary.PreferenceManager
import com.lmx.common_mvvm.network.BaseNetworkApi
import com.lmx.common_mvvm.network.CoroutineCallAdapterFactory
import com.lmx.common_mvvm.network.interceptor.logging.LogInterceptor
import com.lmx.rxjava2retrofit2.const.UrlConst
import com.lmx.baselibrary.mvvm.NetApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by lmx on 2020/5/7
 * Describe :网络请求构建器，继承BasenetworkApi 并实现setHttpClientBuilder/setRetrofitBuilder方法，
 * 在这里可以添加拦截器，设置构造器可以对Builder做任意操作
 */
class NetworkApi : BaseNetworkApi() {

    //封装NetApiService变量 方便直接快速调用
    val service: NetApiService by lazy {
        getApi(NetApiService::class.java, UrlConst.SERVER_URL)
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
//            cache(Cache(File(App.CONTEXT.cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            //添加Cookies自动持久化
            interceptors().add(Interceptor { chain -> // 获取 Cookie
                val resp = chain.proceed(chain.request())
                val cookies = resp.headers("Set-Cookie")
                var cookieStr: String? = ""
                if (cookies.isNotEmpty()) {
                    for (i in cookies.indices) {
                        cookieStr += cookies[i]
                    }
                    PreferenceManager.setUserCookieId(cookieStr)
                }
                resp
            })
            interceptors().add(Interceptor { chain -> // 设置 Cookie
                val cookieStr = PreferenceManager.getUserCookieId("")
                if (!TextUtils.isEmpty(cookieStr)) {
                    chain.proceed(chain.request().newBuilder().header("Cookie", cookieStr).build())
                } else chain.proceed(chain.request())
            })
            //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
//            addInterceptor(HeadInterceptor(mapOf()))
//            addInterceptor(UrlInterceptor())
            //添加缓存拦截器 可传入缓存天数，不传默认7天
//            addInterceptor(CacheInterceptor())
            // 日志拦截器
            addInterceptor(LogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        return builder.apply {
//            addConverterFactory(IGsonFactory.create(gson))//支持gson格式
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addConverterFactory(LenientGsonConverterFactory.create(gson))
            addConverterFactory(ScalarsConverterFactory.create())//支持String类型
            addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持observable类型
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

}



