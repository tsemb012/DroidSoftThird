package com.example.droidsoftthird

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GroupDetailViewModelFactory ( private val repository: UserGroupRepository, private val groupId:String): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupDetailViewModel::class.java)) {
            return GroupDetailViewModel( repository, groupId ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
