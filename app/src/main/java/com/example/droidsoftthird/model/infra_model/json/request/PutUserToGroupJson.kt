package com.example.droidsoftthird.model.infra_model.json.request

import com.squareup.moshi.Json

data class PutUserToGroupJson (
        @Json(name = "user_id")
        val userId: String,
)