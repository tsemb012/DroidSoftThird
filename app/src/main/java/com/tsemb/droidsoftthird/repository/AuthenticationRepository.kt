package com.tsemb.droidsoftthird.repository

interface AuthenticationRepository {
    suspend fun refreshToken(): String?
    suspend fun saveTokenId(tokenId: String)
}
