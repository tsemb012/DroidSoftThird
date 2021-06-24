package com.example.droidsoftthird

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.droidsoftthird.ui.mapPager.MapPagerFragment
import com.example.droidsoftthird.ui.schedule.ApprovedScheduleFragment
import com.example.droidsoftthird.ui.reccomendPager.RecommendPagerFragment
import java.lang.IndexOutOfBoundsException

const val PAGE_COUNT_HOME = 3
const val RECOMMEND_PAGE_INDEX = 0
const val SCHEDULE_PAGE_INDEX = 1
const val MAP_PAGE_INDEX = 2


class HomeViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment){

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        RECOMMEND_PAGE_INDEX to { RecommendPagerFragment() },
        SCHEDULE_PAGE_INDEX to {SchedulePagerFragment()},
        MAP_PAGE_INDEX to { MapPagerFragment() }
    )

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT_HOME
    }

}


const val PAGE_COUNT_SCHEDULE = 2
const val APPROVED_SCHEDULE_PAGE_INDEX = 0
const val PENDING_SCHEDULE_PAGE_INDEX = 1

class ScheduleViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment){


    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        APPROVED_SCHEDULE_PAGE_INDEX to { ApprovedScheduleFragment() },
        PENDING_SCHEDULE_PAGE_INDEX to {SchedulePagerFragment()},
    )

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT_SCHEDULE
    }

}