package com.betrybe.trybnb.data.api

import com.betrybe.trybnb.data.models.LoginRequest
import com.betrybe.trybnb.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("auth")
    suspend fun doLoginRequest(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
