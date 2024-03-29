package com.tsemb.droidsoftthird.repository

import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tsemb.droidsoftthird.*
import com.tsemb.droidsoftthird.api.MainApi
import com.tsemb.droidsoftthird.model.domain_model.fire_model.FireGroup
import com.tsemb.droidsoftthird.model.domain_model.fire_model.RawScheduleEvent
import com.tsemb.droidsoftthird.model.infra_model.json.request.PutUserToEventJson
import com.tsemb.droidsoftthird.model.infra_model.json.request.PutUserToGroupJson
import com.tsemb.droidsoftthird.model.infra_model.json.request.RemoveUserFromEventJson
import com.tsemb.droidsoftthird.repository.DataStoreRepository.Companion.TOKEN_ID_KEY
import com.tsemb.droidsoftthird.repository.csvloader.AssetLoader
import com.tsemb.droidsoftthird.repository.csvloader.CityCsvLoader
import com.tsemb.droidsoftthird.repository.csvloader.PrefectureCsvLoader
import com.tsemb.droidsoftthird.repository.paging.GroupPagingSource
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tsemb.droidsoftthird.Result
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.Category
import com.tsemb.droidsoftthird.model.domain_model.ChatGroup
import com.tsemb.droidsoftthird.model.domain_model.CreateEvent
import com.tsemb.droidsoftthird.model.domain_model.EditedGroup
import com.tsemb.droidsoftthird.model.domain_model.EventDetail
import com.tsemb.droidsoftthird.model.domain_model.EventItem
import com.tsemb.droidsoftthird.model.domain_model.GroupCountByArea
import com.tsemb.droidsoftthird.model.domain_model.SimpleGroup
import com.tsemb.droidsoftthird.model.domain_model.UserDetail
import com.tsemb.droidsoftthird.model.domain_model.ViewPort
import com.tsemb.droidsoftthird.model.domain_model.YolpSimplePlace
import com.tsemb.droidsoftthird.model.domain_model.YolpSinglePlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BaseRepositoryImpl @Inject constructor(
    private val mainApi: MainApi,
    private val dataStore: DataStore<Preferences>,
    private val assetLoader: AssetLoader,
): RailsApiRepository, FirebaseRepository, DataStoreRepository, AssetRepository {
    private val fireStore = FirebaseFirestore.getInstance()//TODO 全てHiltに入れてインジェクトから取得する。
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User is not logged in.") }
    private val urlCache = mutableMapOf<String, String>()

    override suspend fun getUserId() = userId
    override suspend fun fetchStorageImage(imagePath: String): String {
        if (urlCache.containsKey(imagePath)) {
            return urlCache[imagePath] ?: ""
        }
        val url = suspendCoroutine { continuation ->
            val imageReference = FirebaseStorage.getInstance().getReference(imagePath)
            imageReference.downloadUrl.addOnSuccessListener {
                urlCache[imagePath] = it.toString()
                continuation.resume(it.toString())
            }.addOnFailureListener {
                continuation.resume("")
                Log.d("BaseRepositoryImpl", "fetchStorageImage: $it")
            }
        }
        return url
    }

    override suspend fun fetchUserJoinedGroupIds(): List<String> = mainApi.fetchUserJoinedGroupIds(userId)

    override suspend fun fetchUserJoinedSimpleGroups(): List<SimpleGroup> = mainApi.fetchUserJoinedSimpleGroups(userId).map { it.toEntity() }

    override suspend fun saveTokenId(tokenId: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN_ID_KEY)] = tokenId
        }
    }

    override suspend fun clearTokenId() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(TOKEN_ID_KEY))
        }
    }

/*    override suspend fun postNewUser(signup: SignUpJson): User? =
        mainApi.postNewUser(PostSignUp.Request(signup)).body()?.toEntity()
        //TODO Resultを付けて返した方が良いかを検討する。→ Jsonを戻す時の構造体を再検討する。*/

    override suspend fun getGroups(query: String): Result<List<FireGroup>> = getListResult(query, FireGroup::class.java)

    override suspend fun certifyAndRegister(tokenID: String) {
        mapOf("Authorization" to "Bearer $tokenID").let {
            mainApi.postTokenId(it, userId)
        }
    }

    override suspend fun signUpAndFetchToken(email: String, password: String) : Result<String> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful && signInTask.result != null) {
                                        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                                            ?.addOnCompleteListener { idTokenTask ->
                                                if (idTokenTask.isSuccessful && idTokenTask.result?.token != null) {
                                                    continuation.resume(Result.Success(idTokenTask.result?.token!!))
                                                } else {
                                                    continuation.resume(Result.Failure(Exception("idTokenTask is failed")))
                                                }
                                            }
                                    } else {
                                        continuation.resume(Result.Failure(signInTask.exception ?:IllegalStateException()))
                                    }
                                }
                        } else {
                            continuation.resume(Result.Failure(task.exception ?: IllegalStateException()))
                        }
                    }
            }
        }//TODO　ネスト深すぎ。。要リファクタリング

    override suspend fun singIn(email: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful && signInTask.result != null) {
                            FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                                ?.addOnCompleteListener { idTokenTask ->
                                    if (idTokenTask.isSuccessful && idTokenTask.result?.token != null) {
                                        continuation.resume(Result.Success(idTokenTask.result?.token!!))
                                    } else {
                                        continuation.resume(Result.Failure(Exception("idTokenTask is failed")))
                                    }
                                }
                        } else {
                            continuation.resume(Result.Failure(signInTask.exception ?:IllegalStateException()))
                        }
                    }
            }
        }//TODO 要リファクタリング

    override suspend fun getGroup(groupId: String): Result<FireGroup?> = //TODO GroupがNullである可能性のリスクをどこかで回収する。
    withContext(Dispatchers.IO){suspendCoroutine { continuation ->
        fireStore.collection("groups")
            .document(groupId)
            .get()
            .addOnSuccessListener {
                try {
                    continuation.resume(Result.Success(it.toObject()))
                } catch (e: Exception) {
                    continuation.resume(Result.Failure(e))
                }
            }
            .addOnFailureListener {
                continuation.resume(Result.Failure(it))
            }
    }
    }


    override suspend fun uploadPhoto(uri: Uri): Result<StorageReference> {
        val photoRef = fireStorageRef.child("images/${UUID.randomUUID().toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                photoRef.putFile(uri)
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.storage))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    override suspend fun createGroup(group: EditedGroup): String? =
        mainApi.createGroup(group.toJson()).body()?.message

    override suspend fun fetchGroupDetail(groupId: String): ApiGroup {
        val response = mainApi.fetchGroup(groupId)
        if (response.isSuccessful) {
            return response.body()?.toEntity() ?: throw Exception("response body is null")
        } else {
            throw Exception("fetchGroupDetail is failed")
        }
    }

    override suspend fun fetchGroups(
            groupFilterCondition: ApiGroup.FilterCondition
    ) : Flow<PagingData<ApiGroup>> =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = true
            )
        ) {
            GroupPagingSource(mainApi, userId, groupFilterCondition)
        }.flow

    override suspend fun userLeaveGroup(groupId: String): String? {
        val response = mainApi.removeUserFromGroup(groupId, PutUserToGroupJson(userId))
        return if (response.isSuccessful) {
            return response.body()?.message
        } else {
            throw Exception("userLeaveGroup is failed")
        }
    }

    override suspend fun userJoinGroup(groupId: String): String? {
        val response = mainApi.putUserToGroup(groupId, PutUserToGroupJson(userId))
        return if (response.isSuccessful) {
            return response.body()?.message
        } else {
            throw Exception("userJoinGroup is failed")
        }
    }

    override suspend fun fetchJoinedGroups() : List<ApiGroup> = mainApi.fetchUserJoinedGroups(userId = userId).body()?.map { it.toEntity() } ?: listOf()
    override suspend fun fetchGroupCountByArea(): List<GroupCountByArea> = mainApi.fetchGroupCountByArea(userId, false).map { it.toEntity() }
    override suspend fun fetchChatGroup(groupId: String): ChatGroup = mainApi.fetchChatGroup(groupId).body()?.toEntity() ?: throw Exception("fetchChatGroup is failed")
    override suspend fun fetchUser(): UserDetail = mainApi.fetchUser(userId).toEntity()
    override suspend fun updateUserDetail(userDetail: UserDetail) = mainApi.putUserDetail(userId, userDetail.copy(userId = userId).toJson()).message
    override suspend fun createUser(userDetail: UserDetail): String = mainApi.putUserDetail(userId, userDetail.copy(userId = userId).toJson()).message
    override suspend fun createEvent(event: CreateEvent): String = mainApi.postEvent(event.copy(hostId = userId).toJson()).message
    override suspend fun fetchEvents(): List<EventItem> = mainApi.getEvents(userId).map { it.toEntity() }
    override suspend fun fetchEventDetail(eventId: String): EventDetail = mainApi.getEventDetail(eventId, userId).toEntity()
    override suspend fun registerEvent(eventId: String): String = mainApi.putEvent(eventId, PutUserToEventJson(userId)).message
    override suspend fun unregisterEvent(eventId: String): String = mainApi.putEvent(eventId, RemoveUserFromEventJson(userId)).message
    override suspend fun deleteEvent(eventId: String): String = mainApi.deleteEvent(eventId).message

    override suspend fun yolpTextSearch(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace> =
        mainApi.getYolpTextSearch(
                query = query,
                centerLat = centerPoint.latitude,
                centerLng = centerPoint.longitude,
                northLat = viewPort.northEast?.latitude ?: 0.0,
                eastLng = viewPort.northEast?.longitude ?: 0.0,
                southLat = viewPort.southWest?.latitude ?: 0.0,
                westLng = viewPort.southWest?.longitude ?: 0.0,
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun yolpAutoComplete(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace> =
        mainApi.getYolpAutoComplete(
            query = query,
            centerLat = centerPoint.latitude,
            centerLng = centerPoint.longitude,
            northLat = viewPort.northEast?.latitude ?: 0.0,
            eastLng = viewPort.northEast?.longitude ?: 0.0,
            southLat = viewPort.southWest?.latitude ?: 0.0,
            westLng = viewPort.southWest?.longitude ?: 0.0,
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun yolpCategorySearch(viewPort: ViewPort, centerPoint: LatLng, category: Category): List<YolpSimplePlace> =
        mainApi.getYolpCategorySearch(
            query = if (category == Category.LIBRARY) "図書館" else "",
            category = if (category != Category.LIBRARY) category.name.lowercase() else "",
            centerLat = centerPoint.latitude,
            centerLng = centerPoint.longitude,
            northLat = viewPort.northEast?.latitude ?: 0.0,
            eastLng = viewPort.northEast?.longitude ?: 0.0,
            southLat = viewPort.southWest?.latitude ?: 0.0,
            westLng = viewPort.southWest?.longitude ?: 0.0,
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun yolpDetailSearch(placeId: String): YolpSinglePlace.DetailPlace? =
        mainApi.getYolpDetailSearch(
            placeId = placeId,
        ).body()?.toEntity()

    override suspend fun yolpReverseGeocode(lat: Double, lng: Double): YolpSinglePlace.ReverseGeocode? =
        mainApi.getYolpReverseGeocode(
            lat = lat,
            lng = lng,
        ).body()?.toEntity()

    override suspend fun deleteUser(): String = mainApi.deleteUser(userId).message

    override suspend fun checkUserRegistered(): Boolean = mainApi.checkUserRegistered(userId).registered

    override suspend fun updateAuthProfile(authProfileUpdates:UserProfileChangeRequest): Result<Int> {
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                Firebase.auth.currentUser
                    ?.updateProfile(authProfileUpdates)
                    ?.addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(R.string.upload_success))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    ?.addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }


    override suspend fun getSchedules(query: String): Result<List<RawScheduleEvent>> = getListResult(query, RawScheduleEvent::class.java)

    private suspend fun <T> getListResult(query: String, classType: Class<T>): Result<List<T>> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getQuery(query)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects(classType)))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }

    private fun getQuery(query: String): Query {
        return when(query){
            GROUP_ALL ->
                fireStore
                    .collection("groups")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            GROUP_MY_PAGE ->
                fireStore
                    .collection("groups")
                    .whereArrayContains("members",userId)
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            else -> throw IllegalStateException()
        }
    }

    override suspend fun loadPrefectureCsv(): List<PrefectureCsvLoader.PrefectureLocalItem> =
        assetLoader.prefectureCsvLoader.prefectureLocalItems

    override suspend fun loadCityCsv(): List<CityCsvLoader.CityLocalItem> =
        assetLoader.cityCsvLoader.cityLocalItems

    companion object {
        private const val LIMIT = 50L
        private const val LANGUAGE_JP = "ja"//言語設定する際に、直接Preferenceから取得するようにする。
        private const val REGION_JP = "jp"//設定する際に、直接Preferenceから取得するようにする。
        const val GROUP_ALL = "group_all"
        const val GROUP_MY_PAGE = "group_my_page"
    }
}
