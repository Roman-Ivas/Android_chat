package com.example.messanger

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("pages/sendMessage.php")
    fun sendMessage(@Field("text") text: String, @Field("timestamp")timestamp: Long): Call<Void>

    @GET("pages/getMessages.php")
    fun getMessages(): Call<List<Message>>
}