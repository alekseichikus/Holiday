package ru.createtogether.fragment_main.viewModel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.feature_holiday_impl.viewModel.HolidayViewModel
import ru.createtogether.fragment_main.R
import ru.createtogether.fragment_main.databinding.FragmentMainBinding


@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding()
    private val holidayViewModel: HolidayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ss()
        holidayViewModel.loadHolidaysOfDay("03-02-2022")
        Log.d("dfasdsadasd", "sfsdfsdf")
    }

    private fun ss(){
        holidayViewModel.holidaysOfDayResponse.observe(viewLifecycleOwner, {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.forEach {
                        Log.d("Sdfasdasdsad", "sdasdasdasd")
                    }
                }
                Status.ERROR ->{
                    Log.d("Sdfasdasdsad", it.error.toString())
                }
            }
        })
    }

    companion object{
        fun getInstance() = MainFragment().apply {  }
    }
}