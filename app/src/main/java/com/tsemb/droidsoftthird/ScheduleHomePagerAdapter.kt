package com.tsemb.droidsoftthird

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException

class ScheduleHomePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    companion object {
        private const val PAGE_COUNT = 2
        private const val SCHEDULE_REGISTERED_INDEX = 0
        private const val SCHEDULE_PROPOSED_INDEX = 1
    }
    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        SCHEDULE_REGISTERED_INDEX to { ScheduleCalendarFragment() },
        SCHEDULE_PROPOSED_INDEX to { ScheduleListFragment() }
    )

    override fun getItemCount() = PAGE_COUNT
    override fun createFragment(position: Int) = tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()

}
