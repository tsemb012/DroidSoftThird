package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostScheduleEventJson
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.LocalTime

data class ScheduleEvent(
        val hostId: String? = null,
        val name: String,
        val comment: String,
        val date: LocalDate,
        val period: Pair<LocalTime, LocalTime>,
        val place: EditedPlace? = null,
        val groupId: String,
) {
        fun toJson(localDateAdapter: JsonAdapter<LocalDate>, localTimeAdapter: JsonAdapter<LocalTime>): PostScheduleEventJson {
                val (start, end) = period
                return PostScheduleEventJson(
                        hostId = hostId ?: throw IllegalStateException("hostId is null"),
                        name = name,
                        comment = comment,
                        date = localDateAdapter.toJson(date),
                        startTime = localTimeAdapter.toJson(start),
                        endTime = localTimeAdapter.toJson(end),
                        place = place?.toJson(),
                        groupId = groupId
                )
        }
}

data class ScheduleEventForHome(
        val hostId: String? = null,
        val name: String,
        val comment: String,
        val date: LocalDate,
        val period: Pair<LocalTime, LocalTime>,
        val groupId: String,
        val groupName: String,
        val placeName: String?,
)
