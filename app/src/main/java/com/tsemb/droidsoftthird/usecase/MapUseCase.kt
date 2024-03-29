package com.tsemb.droidsoftthird.usecase

import com.tsemb.droidsoftthird.model.domain_model.Category
import com.tsemb.droidsoftthird.model.domain_model.ViewPort

import com.tsemb.droidsoftthird.model.domain_model.YolpSimplePlace
import com.tsemb.droidsoftthird.model.domain_model.YolpSinglePlace
import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapUseCase  @Inject constructor(private val repository: BaseRepositoryImpl) {


    suspend fun searchByText(query: String, viewPort: ViewPort, centerPoint: LatLng,): List<YolpSimplePlace> =
        repository.yolpTextSearch(query, viewPort, centerPoint)

    suspend fun searchByCategory(viewPort: ViewPort, centerPoint: LatLng, category: Category): List<YolpSimplePlace> =
        repository.yolpCategorySearch(viewPort, centerPoint, category)

    suspend fun autoComplete(query: String, viewPort: ViewPort, centerPoint: LatLng): List<YolpSimplePlace> =
        repository.yolpAutoComplete(query, viewPort, centerPoint)

    suspend fun fetchPlaceDetail(placeId: String): YolpSinglePlace.DetailPlace? =
        repository.yolpDetailSearch(placeId)
    suspend fun reverseGeocode(latLng: LatLng): YolpSinglePlace.ReverseGeocode? =
        repository.yolpReverseGeocode(latLng.latitude, latLng.longitude)

    //suspend fun fetchCurrentLocation():  TODO 現在地を取得するようにする。
}