package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.toEntity
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.example.droidsoftthird.repository.BaseRepositoryImpl.Companion.SCHEDULE_REGISTERED_ALL
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleRegisteredViewModel @ViewModelInject constructor(private val repository: BaseRepositoryImpl): ScheduleViewModel(){


    fun fetchAllSchedules() { fetchSchedules(SCHEDULE_REGISTERED_ALL) }
    private fun fetchSchedules(query: String){
        val job = viewModelScope.launch {
                repository.getSchedules(query).apply {
                    when(this) {
                        is Result.Success -> {
                            data.map { it.toEntity() }.let {
                                schedulesState.value = LoadState.Loaded(it)
                                selectedEvents.value = it.mapNotNull { scheduleEvent ->
                                    if (scheduleEvent.date == LocalDate.now()) { scheduleEvent } else null
                                }
                            }
                        }
                        is Result.Failure -> schedulesState.value = LoadState.Error(exception)
                    }
                }
        }
        schedulesState.value = LoadState.Loading(job)
        job.start()
    }

    fun initializeSchedulesState() {
        schedulesState.value = LoadState.Initialized
    }
}
