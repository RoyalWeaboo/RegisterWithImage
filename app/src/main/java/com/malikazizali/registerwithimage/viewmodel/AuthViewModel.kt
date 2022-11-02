package com.malikazizali.registerwithimage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malikazizali.registerwithimage.model.ResponseRegister
import com.malikazizali.registerwithimage.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel(){
    var addLiveDataUser : MutableLiveData<ResponseRegister>

    init {
        addLiveDataUser = MutableLiveData()
    }

    fun postLiveDataCar() : MutableLiveData<ResponseRegister> {
        return addLiveDataUser
    }

    fun postApiRegister(fullName : RequestBody, email : RequestBody, password : RequestBody, phoneNumber : RequestBody, address : RequestBody,image : MultipartBody.Part, city : RequestBody,){
        RetrofitClient.instance.addUser(fullName, email, password, phoneNumber, address, image, city)
            .enqueue(object : Callback<ResponseRegister> {
                override fun onResponse(
                    call: Call<ResponseRegister>,
                    response: Response<ResponseRegister>
                ) {
                    if (response.isSuccessful){
                        addLiveDataUser.postValue(response.body())
                    }else{
                        addLiveDataUser.postValue(response.body())
                    }

                }

                override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                    //do nothing
                }

            })
    }
}