package com.example.droidsoftthird.composable.group.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.droidsoftthird.MyPageViewModel
import com.example.droidsoftthird.composable.group.content.MyPageGroupList

@Composable
fun MyPageScreen(viewModel: MyPageViewModel, navigateToGroupDetail: (String) -> Unit) {

    viewModel.groupsLoadState.observeAsState().value?.let {
        MyPageGroupList(it, navigateToGroupDetail)
    }
}
