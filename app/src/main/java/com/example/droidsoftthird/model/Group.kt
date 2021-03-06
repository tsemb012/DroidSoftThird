package com.example.droidsoftthird.model

import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Group (//FireStoreで使用する場合は、Nullableかつ初期値にNullを入れる必要がある。
    val hostUserId: String? = null,
    val storageRef: String? = null,
    val groupName: String? = null,
    val groupIntroduction: String? = null,
    val groupType: String? = null,
    val prefecture: String? = null,
    val city: String? = null,
    val facilityEnvironment: String? = null,
    val basis: String? = null,
    val frequency:String? = null,
    val minAge:Int? = null,
    val maxAge:Int? = null,
    val minNumberPerson:Int? = null,
    val maxNumberPerson :Int? = null,
    @field:JvmField
    val isChecked:Boolean? = null,
    val members:List<String>?=null,
    @DocumentId
    val groupId: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null
        //TODO DateAPI　から　Date &TimeAPIに変換する。
        //TODO RawGroupからGroupに変換する使用に変更する。GroupのPOJO見直しする際に。
)
