package com.tsemb.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.databinding.FragmentGroupDetailBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupDetailFragment : Fragment() {

    @Inject
    lateinit var groupDetailViewModelAssistedFactory: GroupDetailViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentGroupDetailBinding = inflate(inflater,
            R.layout.fragment_group_detail, container, false)
        setupNavAppBar(binding)

        val groupId = GroupDetailFragmentArgs.fromBundle(requireArguments()).groupId
        val viewModel = groupDetailViewModelAssistedFactory.create(groupId)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.groupDetail.observe(viewLifecycleOwner) {
            val isJoined = it?.members?.map { member -> member.userId }?.contains(FirebaseAuth.getInstance().currentUser?.uid)
            if(isJoined == true) {
                binding.joinGroupFab.visibility = View.GONE
                binding.leaveGroupFab.visibility = View.VISIBLE
            } else {
                binding.joinGroupFab.visibility = View.VISIBLE
                binding.leaveGroupFab.visibility = View.GONE
            }

            if (viewModel.isHostGroup) binding.leaveGroupFab.visibility = View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }


        viewModel.confirmJoin.observe(viewLifecycleOwner, EventObserver{
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confrimation_join_group)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.userJoinGroup()
                }
                .setNeutralButton(R.string.cancel) { _, _ ->
                }
                .show()
        })

        viewModel.confirmLeave.observe(viewLifecycleOwner, EventObserver{
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confrimation_leave_group)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.userLeaveGroup()
                }
                .setNeutralButton(R.string.cancel) { _, _ ->
                }
                .show()
        })

        viewModel.navigateToMyPage.observe(viewLifecycleOwner, Observer { groupId ->
            groupId?.let {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(
                        when (viewModel.navigateToMyPage.value ) {
                            "fromMyPage" -> R.id.myPageFragment
                            "fromHome" -> R.id.homeFragment
                            else -> R.id.myPageFragment
                        },
                        true
                    )  // popUpToInclusiveをtrueに設定して、groupDetailFragmentまでのバックスタックをクリアします
                    .build()

                this.findNavController().navigate(
                    GroupDetailFragmentDirections.actionGroupDetailFragmentToMyPageFragment(),
                    navOptions
                )
                viewModel.onMyPageNavigated()
            }
        })

        return binding.root
    }

    private fun setupNavAppBar(binding: FragmentGroupDetailBinding) {
        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.collapsingToolbarLayout
        val toolbar: Toolbar = binding.materialToolbar
        toolbar.title = " "

        //-----NavUI Objects
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(
            layout,
            toolbar,
            navController,
            appBarConfiguration
        )
    }
}
