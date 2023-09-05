package com.pk4us.natifegiphyappkotlin

import retrofit2.Call
import retrofit2.http.GET

interface DataService {
    @GET("gifs/trending?api_key=Ke8WRRGTENw3XCtrnYgFqyct2xEK6NEP")
    fun getGifs(): Call<DataResult>
}