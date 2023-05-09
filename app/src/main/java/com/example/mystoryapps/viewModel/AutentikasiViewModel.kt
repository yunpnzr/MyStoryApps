package com.example.mystoryapps.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.api.ApiConfig
import com.example.mystoryapps.preferences.UserPreferences
import com.example.mystoryapps.response.LoginResponse
import com.example.mystoryapps.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutentikasiViewModel (private val preference : UserPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val registerResponse = MutableLiveData<RegisterResponse>()
    val _registerResponse : LiveData<RegisterResponse> = registerResponse

    private val loginResponse =  MutableLiveData<LoginResponse>()

    companion object{
        private const val TAG = "AutentikasiViewModel"
    }

    fun register (token: Context, name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    registerResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun login (token: Context, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    loginResponse.value = response.body()

                    val token = response.body()!!.loginResult?.token
                    val userId = response.body()!!.loginResult?.userId
                    val name = response.body()!!.loginResult?.name
                    viewModelScope.launch {
                        if (token != null) {
                            if (userId != null) {
                                if (name != null) {
                                    preference.saveLoginSession(token, userId, name)
                                    Log.d("AutentikasiViewModel", "Saving token=$token, userId=$userId, name=$name")
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserPreferences(property:String): LiveData<String> {
        return when(property){
            "userId" -> preference.getUserId().asLiveData()
            "token" -> preference.getToken().asLiveData()
            "name" -> preference.getName().asLiveData()
            else -> preference.getUserId().asLiveData()
        }.also { liveData ->
            liveData.observeForever {
                Log.e("UserPreferences", "Retrieved user preference: $property =$it")
            }
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch {
            preference.clearLoginSession()
        }
    }
}