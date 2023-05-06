package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.infra_model.json.request.PostGroupJson
import kotlin.math.pow

data class EditedGroup(
    val groupId: String?,
    val hostUserId: String,
    val storageRef: String,
    val groupName: String,
    val groupIntroduction: String,
    val groupType: GroupType,
    val prefecture_code: Int,
    val city_code: Int,
    val isOnline: Boolean,
    val facilityEnvironment: FacilityEnvironment,
    val basis: FrequencyBasis,
    val frequency:Int,
    val minAge:Int,
    val maxAge:Int,
    val maxNumberPerson:Int,
    val isChecked:Boolean,
) {
        fun toJson() =
                PostGroupJson(
                        hostId = hostUserId,
                        name = groupName,
                        introduction = groupIntroduction,
                        groupType = groupType.name.lowercase(),
                        prefecture_code = prefecture_code,
                        city_code = city_code,
                        isOnline = isOnline,
                        facilityEnvironment = facilityEnvironment.name.lowercase(),
                        frequencyBasis = basis.name.lowercase(),
                        frequencyTimes = frequency,
                        maxAge = maxAge,
                        minAge = minAge,
                        maxNumber = maxNumberPerson,
                        isSameSexuality = isChecked,
                        imageUrl = storageRef,
                )
}

data class ApiGroup(//TODO ApiGroupからGroupに名前を変更する//Firebaseのモデルが全て置き換わったら
    val groupId: String?,
    val hostUserId: String,
    val storageRef: String,
    val groupName: String,
    val groupIntroduction: String,
    val groupType: GroupType,
    val prefecture: String?,//TODO Areaに変更する。
    val city: String?,
    val isOnline: Boolean,
    val facilityEnvironment: FacilityEnvironment,
    val basis: FrequencyBasis,
    val frequency:Int,
    val minAge:Int,
    val maxAge:Int,
    val maxNumberPerson:Int,
    val isChecked:Boolean,
) {
     data class FilterCondition(
         val areaCode: Int? = null,
         val areaCategory: AreaCategory? = null,
         val groupTypes: Set<GroupType> = setOf(),
         val facilityEnvironments: Set<FacilityEnvironment> = setOf(),
         val frequencyBasis: FrequencyBasis? = null,
     ) {
         companion object {
             fun getPrefectureCode(number: Int): Int {
                 if (number <= 47) return number
                 val divisor = 10.0.pow(3.toDouble()).toInt()
                 return number / divisor
             }
         }

         enum class AreaCategory { PREFECTURE, CITY, }
     }
}

interface GroupOption { val displayNameId: Int }
enum class GroupType(override val displayNameId: Int): GroupOption {
    SEMINAR(R.string.seminar),
    WORKSHOP(R.string.workshop),
    MOKUMOKU(R.string.mokumoku),
    OTHER_GROUP_TYPE(R.string.other),
    NONE_GROUP_TYPE(R.string.no_set);

    companion object {
        fun toArrayForDisplay() = values().filter { it != NONE_GROUP_TYPE }
    }
}

enum class FacilityEnvironment(override val displayNameId: Int): GroupOption {
    LIBRARY(R.string.library),
    CAFE_RESTAURANT(R.string.cafe_restaurant),
    RENTAL_SPACE(R.string.rental_space),
    CO_WORKING_SPACE(R.string.co_working_space),
    PAID_STUDY_SPACE(R.string.paid_study_space),
    PARK(R.string.park),
    ONLINE(R.string.online),
    OTHER_FACILITY_ENVIRONMENT(R.string.other),
    NONE_FACILITY_ENVIRONMENT(R.string.no_set), ;

    companion object {
        fun toArrayForDisplay() = FacilityEnvironment.values().filter { it != NONE_FACILITY_ENVIRONMENT }
    }
}

enum class FrequencyBasis(override val displayNameId: Int): GroupOption {
    DAILY(R.string.daily),
    WEEKLY(R.string.weekly),
    MONTHLY(R.string.monthly),
    NONE_FREQUENCY_BASIS(R.string.no_set);

    companion object {
        fun toArrayForDisplay() = FrequencyBasis.values().filter { it != NONE_FREQUENCY_BASIS }
    }
}
