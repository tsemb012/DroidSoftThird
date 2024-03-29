package com.tsemb.droidsoftthird.composable.group.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.shared.EmptyMessage
import com.tsemb.droidsoftthird.composable.shared.FundamentalSheet
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.FacilityEnvironment
import com.tsemb.droidsoftthird.model.domain_model.FrequencyBasis
import com.tsemb.droidsoftthird.model.domain_model.GroupType
import com.tsemb.droidsoftthird.model.domain_model.Style
import kotlinx.coroutines.flow.flowOf

@Composable
fun PagingGroupList(lazyPagingGroups: LazyPagingItems<ApiGroup>, navigate: (String) -> Unit, isLocationGroup: Boolean = false) {

    val isLoading = lazyPagingGroups.loadState.refresh is LoadState.Loading || lazyPagingGroups.loadState.append is LoadState.Loading
    val error = lazyPagingGroups.loadState.refresh as? LoadState.Error ?: lazyPagingGroups.loadState.append as? LoadState.Error

    FundamentalSheet(
        content = {
            PagingGroupListContent(
                isLoading,
                lazyPagingGroups,
                isLocationGroup,
                navigate,
            )
        },
        isLoading = isLoading,
        error = error?.error
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun PagingGroupListContent(
    isLoading: Boolean,
    lazyPagingGroups: LazyPagingItems<ApiGroup>,
    isLocationGroup: Boolean,
    navigate: (String) -> Unit,
) {

    var refreshing by remember { mutableStateOf(false) }
    fun refresh() {lazyPagingGroups.refresh()}
    val state = rememberPullRefreshState(refreshing, ::refresh)
    val listState = rememberLazyListState()

    LaunchedEffect(true) {
        listState.scrollToItem(
            listState.firstVisibleItemIndex,
            listState.firstVisibleItemScrollOffset
        )
    }

    Column(modifier = Modifier.pullRefresh(state)) {
        if (!isLoading && lazyPagingGroups.itemCount != 0 && isLocationGroup) {
            lazyPagingGroups[0]?.let {
                it.prefecture
                Text(
                    text = "${it.prefecture}、${it.city}",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
            Divider()
        }
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                state = listState,
            ) {
                if (!isLoading && lazyPagingGroups.itemCount == 0) item { EmptyMessage(R.string.no_groups_found) }
                items(lazyPagingGroups) {
                    it?.let { group -> GroupListItem(group, navigate) }
                }
            }
            if (!isLocationGroup) PullRefreshIndicator(
                refreshing,
                state,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true, showSystemUi = true)
@Composable
fun GroupListEmptyPreview() {
    val group1 = ApiGroup(
        groupId = "1", hostUserId = "1", groupName = "group1", groupIntroduction = "group1",
        groupType = GroupType.INDIVIDUAL_TASK, prefecture = "prefecture", city = "city", isOnline = false,
        facilityEnvironment = FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT, basis = FrequencyBasis.MONTHLY, style = Style.FOCUS,
        frequency = 1, minAge = 10, maxAge = 40, maxNumberPerson = 10, isChecked = false, storageRef = "https://images.pexels.com/photos/9954174/pexels-photo-9954174.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"

    )
    val group2 = ApiGroup(
        groupId = "2", hostUserId = "2", groupName = "group2", groupIntroduction = "group2",
        groupType = GroupType.SHARED_GOAL, prefecture = "prefecture", city = "city", isOnline = false,
        facilityEnvironment = FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT, basis = FrequencyBasis.MONTHLY, style = Style.FOCUS,
        frequency = 1, minAge = 10, maxAge = 40, maxNumberPerson = 10, isChecked = false, storageRef = "https://images.pexels.com/photos/17079798/pexels-photo-17079798.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")
    val group3 = ApiGroup(
        groupId = "3", hostUserId = "3", groupName = "group3", groupIntroduction = "group3",
        groupType = GroupType.SHARED_GOAL, prefecture = "prefecture", city = "city", isOnline = false,
        facilityEnvironment = FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT, basis = FrequencyBasis.MONTHLY, style = Style.FOCUS,
        frequency = 1, minAge = 10, maxAge = 40, maxNumberPerson = 10, isChecked = false, storageRef = "https://images.pexels.com/photos/14880025/pexels-photo-14880025.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")
    val pagingData = PagingData.from(listOf(group1, group2, group3))
    val collectAsLazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()
    Surface(color = Color.White) {
        PagingGroupList(lazyPagingGroups = collectAsLazyPagingItems as LazyPagingItems<ApiGroup>, navigate = {})
    }

}
