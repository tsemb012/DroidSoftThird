package com.tsemb.droidsoftthird.model.infra_model.json.request

import com.tsemb.droidsoftthird.model.domain_model.EditedPlace
import com.tsemb.droidsoftthird.model.domain_model.Location
import com.squareup.moshi.Json

data class PostEventJson(
    @Json(name = "host_id")
        val hostId: String,
    val name: String,
    val comment: String,
    @Json(name = "start_date_time")
        val startDateTime: String,
    @Json(name = "end_date_time")
        val endDateTime: String,
    val place: PostPlaceJson?,
    @Json(name = "group_id")
        val groupId: String
)

data class PostPlaceJson(
        @Json(name = "place_id")
        val placeId: String,
        val name: String,
        val yomi: String?,
        val latitude: Double,
        val longitude: Double,
        val address: String?,
        val category: String?,
        val tel: String?,
        val url: String?,
        val memo: String?
) {
        fun toEntity(): EditedPlace {
                return EditedPlace(
                        placeId = placeId,
                        name = name,
                        yomi = yomi,
                        category = category,
                        location = Location(lat = latitude, lng = longitude),
                        formattedAddress = address,
                        tel = tel,
                        url = url,
                        memo = memo,
                )
        }
}
