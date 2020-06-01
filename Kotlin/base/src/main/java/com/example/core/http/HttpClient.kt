package com.example.core.http

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

class HttpClient private constructor() : OkHttpClient() {

    fun <T> get(path: String, type: Type, entityCallback: EntityCallback<T>) {
        val request = Request.Builder()
                .url("https://api.hencoder.com/$path")
                .build()
        val call = INSTANCE.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                entityCallback.onFailure("网络异常")
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code()) {
                    in 200 until 300 -> {
                        response.body()?.string()?.let {
                            entityCallback.onSuccess(convert<Any>(it, type) as T)
                        } ?: entityCallback.onFailure("数据载入失败")
                    }
                    in 400 until 500 -> {
                        entityCallback.onFailure("客户端错误")
                    }
                    in 500 until 600 -> {
                        entityCallback.onFailure("服务器错误")
                    }
                    else -> {
                        entityCallback.onFailure("未知错误")
                    }
                }
            }
        })
    }

    companion object {

        val INSTANCE = HttpClient()
        private val gson = Gson()
        private fun <T> convert(json: String, type: Type): T {
            return gson.fromJson(json, type)
        }
    }
}