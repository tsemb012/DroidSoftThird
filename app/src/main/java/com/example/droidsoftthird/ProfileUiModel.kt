package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.model.presentation_model.LoadState

data class ProfileUiModel (
    val rawUserDetail: UserDetail = initializedUserDetail,
    val editedUserDetail: UserDetail = initializedUserDetail,
    val temporalUserImage: Map<String, String>? = null,
    val isSubmitEnabled: Boolean = false,
    val loadState: LoadState = LoadState.Initialized,
) {
    val gender = editedUserDetail.gender.let { it.ifBlank { NO_SETTING } }
    val age = editedUserDetail.age.toString().let { if(it == "-1") NO_SETTING else  it }
    val area = editedUserDetail.area.let {
        if (it == null)  NO_SETTING
        else if (it.city == null) it.prefecture?.name ?: NO_SETTING
        else it?.prefecture?.name + ", " + it?.city?.name
    }

    companion object {
        operator fun invoke(
            current: ProfileUiModel,
            _rawUserDetail: UserDetail,
            _editedUserDetail: UserDetail,
            _temporalUserImage: Map<String, String>,
            isTextFilled: Boolean,
            _loadState: LoadState,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                editedUserDetail = _editedUserDetail,
                temporalUserImage = _temporalUserImage,
                isSubmitEnabled = isValid(_rawUserDetail, _editedUserDetail, _temporalUserImage, isTextFilled),
                loadState = _loadState,
        )

        private const val NO_SETTING = "未設定"

        private fun isValid(
                rawUserDetail: UserDetail,
                editedUserDetail: UserDetail,
                temporalUserImage: Map<String, String>,
                isTextFilled: Boolean
        ) =
                isChangedUserDetail(rawUserDetail, editedUserDetail) &&
                isStoredTemporalImages(temporalUserImage) &&
                isNotEmptyUserDetail(editedUserDetail) &&
                isTextFilled

        private fun isChangedUserDetail(raw: UserDetail, edited: UserDetail) = raw != edited
        private fun isStoredTemporalImages(temporalUserImage: Map<String, String>) = temporalUserImage.isNotEmpty()
        private fun isNotEmptyUserDetail(edited: UserDetail) =
                edited.gender.isNotBlank() &&
                edited.area != null &&
                edited.age != -1
        }

    }
