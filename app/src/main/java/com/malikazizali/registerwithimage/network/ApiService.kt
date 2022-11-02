package com.malikazizali.registerwithimage.network

import com.malikazizali.registerwithimage.model.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/register")
    @Multipart
    fun addUser(
        @Part("full_name") name: RequestBody,
        @Part("email") email : RequestBody,
        @Part("password") password : RequestBody,
        @Part("phone_number") phone_number : RequestBody,
        @Part("address") address : RequestBody,
        @Part image_url : MultipartBody.Part,
        @Part("city") city : RequestBody
    ): Call<ResponseRegister>
}