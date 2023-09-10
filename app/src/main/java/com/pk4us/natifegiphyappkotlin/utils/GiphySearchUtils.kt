package com.pk4us.natifegiphyappkotlin.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import com.pk4us.natifegiphyappkotlin.data.DataResult
import com.pk4us.natifegiphyappkotlin.data.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun searchGifs(query: String, searchGifsState: MutableState<List<String>>) {
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val retroService = retrofit.create(DataService::class.java)
    retroService.searchGifs(query).enqueue(object : Callback<DataResult?> {
        override fun onResponse(
            call: Call<DataResult?>, response: Response<DataResult?>
        ) {
            val body = response.body()
            if (body == null) {
                Log.d(TAG, "onResponse: No response...")
                return
            }

            if (response.isSuccessful) {
                val gifUrls =
                    response.body()?.res?.map { it.images.ogImage.url } ?: emptyList()
                searchGifsState.value = gifUrls
            } else {
                Log.d(TAG, "onResponse: No response...")
            }
        }

        override fun onFailure(call: Call<DataResult?>, t: Throwable) {
            Log.e(TAG, "onFailure: ${t.message}", t)
        }
    })
}
