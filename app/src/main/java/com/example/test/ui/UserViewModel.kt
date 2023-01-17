package com.example.test.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.api.UserRepository
import com.example.test.model.User
import com.example.test.uitls.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.activity.viewModels
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val repo: UserRepository): ViewModel() {

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>> = _userLiveData

    suspend fun getUserNum(username: String, pin : Int){
        _userLiveData.postValue(Resource.Loading())
        val response = repo.getUserNum(username,pin)
        try {
            if (response.isSuccessful){
                _userLiveData.postValue(Resource.Success(response.body()!!))
            }else{
                _userLiveData.postValue(Resource.Error(response.message()))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> _userLiveData.postValue(Resource.Error("No Internet"))
                else -> _userLiveData.postValue(Resource.Error(t.message.toString()))

            }
        }

    }



}