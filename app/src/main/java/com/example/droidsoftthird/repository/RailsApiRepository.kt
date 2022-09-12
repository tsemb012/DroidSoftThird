package com.example.droidsoftthird.repository

import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.fire_model.Group
import com.example.droidsoftthird.model.json.SignUpJson

interface RailsApiRepository {

    suspend fun postNewUser(singup: SignUpJson): User?

    suspend fun createGroup(group: Group): String?

}