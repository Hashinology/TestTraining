package com.example.test.api

import javax.inject.Inject

class UserRepository @Inject constructor(val api : ApiService) {

    suspend fun getUserNum(username: String , pin: Int) =
        api.getUserNum(username,pin)
}