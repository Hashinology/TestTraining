package com.example.test.api

import com.example.test.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("getaccount/{username}/{pin}")
    suspend fun getUserNum(@Path("username")  username:String,
                           @Path("pin") pin: Int): Response<User>
}