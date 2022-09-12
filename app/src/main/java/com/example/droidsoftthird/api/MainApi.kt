package com.example.droidsoftthird.api

import com.example.droidsoftthird.model.json.UserJson
import com.example.droidsoftthird.model.request.PostGroup
import com.example.droidsoftthird.model.request.PostSignUp
import com.example.droidsoftthird.model.response.MessageResponse
import retrofit2.Response
import retrofit2.http.*

interface MainApi {

    @GET("signup")
    suspend fun postTokenId(
            @HeaderMap headers: Map<String, String>,
    ) : Response<Any>

    @POST("users")
    fun postNewUser(@Body request: PostSignUp.Request): Response<UserJson>

    @POST("groups")
    suspend fun createGroup(@Body request: PostGroup.Request): Response<MessageResponse>
}
