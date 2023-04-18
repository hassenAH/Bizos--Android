package devsec.app.rhinhorealestates.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance

class RegisterViewModel : ViewModel() {
    val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

    fun register() {
//        GlobalScope.launch {
//            val response = retIn.registerUser()
//            if (response.isSuccessful) {
//                println("Response: ${response.body()}")
//            } else {
//                println("Response: ${response.errorBody()}")
//            }



        }
    }