package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.compose.navigate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.droidsoftthird.databinding.FragmentMyPageBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment: Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true);
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        requireActivity().actionBar?.title = getString(R.string.my_page)

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val adapter = GroupAdapter(GroupListener{ groupId, groupName ->
            viewModel.onGroupClicked(groupId, groupName)
        })
        binding.groupList.adapter = adapter

        viewModel.getMyGroups()

        viewModel.groups.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapter.submitList(it) }
        })


        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ groupInfo ->
            groupInfo?.let {

                val action =
                    MyPageFragmentDirections.actionMyPageFragmentToChatRoomFragment(it.first,it.second)
                navController.navigate(action)
                viewModel.onChatRoomNavigated()
            }
        })//DONE Navigation??????????????????????????????????????????????????????

        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home,menu)
        val primaryWhite = "#F6FFFE"
        menu.getItem(0).icon.apply {
            mutate() // Drawable????????????????????????
            setColorFilter(android.graphics.Color.parseColor(primaryWhite), PorterDuff.Mode.SRC_ATOP) // ???????????????????????????
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.filter -> {
                TODO("Filter???????????????????????????")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
