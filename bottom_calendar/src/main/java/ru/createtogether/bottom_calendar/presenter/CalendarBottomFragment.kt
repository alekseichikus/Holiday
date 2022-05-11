package ru.createtogether.bottom_calendar.presenter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.feature_day_utils.model.DayModel
import ru.createtogether.feature_day_utils.model.MonthModel
import ru.createtogether.bottom_calendar.R
import ru.createtogether.bottom_calendar.customView.MonthView
import ru.createtogether.bottom_calendar.databinding.BottomDialogCalendarBinding
import ru.createtogether.bottom_calendar.helpers.MonthAdapterListener
import ru.createtogether.bottom_calendar.presenter.viewModel.CalendarViewModel
import ru.createtogether.common.helpers.baseFragment.BaseBottomDialogFragment
import com.example.feature_adapter_generator.initAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.feature_info_board.helpers.InfoBoardListener
import java.util.*

@AndroidEntryPoint
class CalendarBottomFragment : BaseBottomDialogFragment(R.layout.bottom_dialog_calendar) {

    private val binding: BottomDialogCalendarBinding by viewBinding()
    private val calendarViewModel: CalendarViewModel by viewModels()

    private val baseHolidayViewModel: BaseHolidayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initObservers()

        loadCalendars()
    }

    private fun loadCalendars() {
        calendarViewModel.loadCalendars(
            Calendar.getInstance().apply {
                timeInMillis = this@CalendarBottomFragment.time
                set(Calendar.DAY_OF_MONTH, 1)
            }, 0, Calendar.getInstance().apply { timeInMillis = this@CalendarBottomFragment.time })
    }

    private fun setListeners() {
        setCloseClick()
        setTryAgainClick()
        setCalendarClick()
    }

    private fun setCalendarClick() {
        binding.ivCalendar.setOnClickListener {
            BaseCalendarBottomFragment.getInstance(time = time).show(parentFragmentManager, null)
            dismiss()
        }
    }

    private fun setCloseClick() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setTryAgainClick() {
        binding.infoBoardView.setInfoBoardListener(object : InfoBoardListener {
            override fun onActionClick() {
                loadCalendars()
            }
        })
    }

    private fun initObservers() {
        observeMonthsResponse()
        observeHolidaysOfMonthResponse()
    }

    private fun observeHolidaysOfMonthResponse() {
        lifecycleScope.launch {
            baseHolidayViewModel.holidaysOfMonthResponse.collect {
                when (it) {
                    is Event.Loading -> {
                    }
                    is Event.Success -> {
                        hideContent()
                        binding.rvMonths.show()

                        initCalendarAdapter(it.data)
                    }
                    is Event.Error -> {
                        hideContent()
                        showInternetError()
                    }
                }
            }
        }
    }

    private fun showInternetError() {
        with(binding.infoBoardView) {
            binding.infoBoardView.setContent(
                getString(R.string.error_internet),
                getString(R.string.error_internet_description),
                R.drawable.ic_alien, R.string.button_try_again
            )
            show()
        }
    }

    private fun hideContent() {
        with(binding) {
            rvMonths.gone()
            progressBar.gone()
            infoBoardView.gone()
            mbDate.gone()
        }
    }

    private fun observeMonthsResponse() {
        lifecycleScope.launch {
            calendarViewModel.loadCalendarsResponse.collect {
                when (it) {
                    is Event.Loading -> {
                        hideContent()
                        binding.progressBar.show()
                    }
                    is Event.Success -> {
                        baseHolidayViewModel.loadHolidaysOfMonth(
                            Calendar.getInstance().apply {
                                timeInMillis = this@CalendarBottomFragment.time
                                set(Calendar.DAY_OF_MONTH, 1)
                            }.time.withPattern(Constants.DEFAULT_DATE_PATTERN), it.data
                        )
                    }
                    is Event.Error -> {
                    }
                }
            }
        }
    }

    private fun onDayClick(day: DayModel) {
        parentFragmentManager.setFragmentResult(
            CALENDAR_REQUEST,
            bundleOf(DATE_LONG to day.calendar.time.time)
        )
        dismiss()
    }

    private fun initCalendarAdapter(months: List<MonthModel>) {
        binding.rvMonths.initAdapter(
            months,
            MonthView::class.java,
            object : MonthAdapterListener {
                override fun onDayClick(day: DayModel) {
                    this@CalendarBottomFragment.onDayClick(day = day)
                }

                override fun onClick(item: MonthModel) {

                }
            },
            object :
                com.example.feature_adapter_generator.DiffUtilTheSameCallback<MonthModel> {
                override fun areItemsTheSame(oldItem: MonthModel, newItem: MonthModel) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: MonthModel, newItem: MonthModel) =
                    oldItem.days == newItem.days
            })
    }

    private val time by lazy {
        requireArguments().getLong(PARAM_TIME)
    }

    companion object {
        const val CALENDAR_REQUEST = "calendar_request"
        const val DATE_LONG = "date_long"

        private const val PARAM_TIME = "param_time"

        fun getInstance(time: Long) = CalendarBottomFragment().apply {
            arguments = bundleOf(PARAM_TIME to time)
        }
    }
}