package com.tsemb.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsemb.droidsoftthird.composable.group.screen.RecommendGroupsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendGroupsFragment:Fragment() {

    private val viewModel : RecommendGroupsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
        )
        setContent {
            RecommendGroupsScreen(
                viewModel,
                ::navigateToGroupDetail,
                ::navigateToGroupAdd
            )
        }
    }

    private fun navigateToGroupDetail(groupId: String) {
       findNavController().navigate(
           HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
        )
    }

    private fun navigateToGroupAdd() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
        )
    }
}