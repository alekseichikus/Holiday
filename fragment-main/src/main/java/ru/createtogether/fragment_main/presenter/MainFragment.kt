package ru.createtogether.fragment_main.presenter

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.squareup.moshi.JsonDataException
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.bottom_calendar.presenter.CalendarBottomFragment
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.Utils
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_holiday.adapter.HolidayShortAdapter
import ru.createtogether.feature_holiday_impl.viewModel.HolidayViewModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_info_board.helpers.InfoBoardListener
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.feature_worker_impl.di.WorkerModule
import ru.createtogether.fragment_about.AboutFragment
import ru.createtogether.fragment_favorite.presenter.FavoriteFragment
import ru.createtogether.fragment_holiday.HolidayFragment
import ru.createtogether.fragment_main.R
import ru.createtogether.fragment_main.databinding.FragmentMainBinding
import ru.createtogether.fragment_main.presenter.viewModel.MainViewModel
import ru.createtogether.fragment_photo.presenter.PhotoFragment
import java.lang.IllegalArgumentException
import java.util.*


@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main), IMainFragment {
    private val binding: FragmentMainBinding by viewBinding()
    private val holidayViewModel: HolidayViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        initListeners()
        initObservers()

        loadHolidaysOfDay()
        holidayViewModel.loadNextDateWithHolidays(mainViewModel.currentDate.withPattern(Constants.DEFAULT_DATE_PATTERN))
    }

    private fun configureViews() {
        binding.clScrollContainer.setPaddingTopMenu()
    }

    private fun initListeners() {
        setCalendarClick()
        setCalendarResult()
        setFavoriteClick()
        setHolidayClick()
        setHolidayListener()
        setTryAgainClick()
        setSearchClick()
        setMenuClick()
        setGoBackClick()
        setGoToNextDayClick()
    }

    private fun setGoBackClick() {
        binding.mbGoBack.setOnClickListener {
            mainViewModel.currentDate = Calendar.getInstance().time
            holidayViewModel.loadHolidaysOfDay(Utils.convertDateToDateString(mainViewModel.currentDate))
        }
    }

    private fun setGoToNextDayClick() {
        binding.holidaysOfCurrentDayEmptyView.setGoToClickListener {
            mainViewModel.currentDate = Calendar.getInstance().setDateString(it).time
            holidayViewModel.loadHolidaysOfDay(
                Utils.convertDateToDateString(
                    mainViewModel.currentDate
                )
            )
        }
    }

    private fun setMenuClick() {
        binding.ivMenu.setOnClickListener {
            onOpen(AboutFragment.getInstance())
        }
    }

    private fun setSearchClick() {
        binding.ivSearch.setOnClickListener {
            (requireActivity() as MainActions).showSnackBar(R.string.coming_soon)
        }
    }

    private fun setTryAgainClick() {
        binding.infoBoardView.setInfoBoardListener(object : InfoBoardListener {
            override fun onActionClick() {
                loadHolidaysOfDay()
            }
        })
    }

    private fun setHolidayListener() {
        binding.holidayView.setLikeClickListener {
            changeLike(holiday = it)
        }
    }

    private fun setFavoriteClick() {
        binding.ivFavorite.setOnClickListener {
            onOpen(FavoriteFragment.getInstance())
        }
    }

    private fun setHolidayClick() {
        binding.holidayView.setOnClickListener {
            onOpen(HolidayFragment.getInstance(holiday = binding.holidayView.holiday))
        }
    }

    private fun setCalendarResult() {
        childFragmentManager.setFragmentResultListener(
            CalendarBottomFragment.CALENDAR_REQUEST, viewLifecycleOwner
        ) { _, result ->
            result.getLong(CalendarBottomFragment.DATE_LONG).let {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it
                mainViewModel.currentDate = calendar.time
                loadHolidaysOfDay()
            }
        }
    }

    private fun changeLike(holiday: HolidayModel) {
        (requireActivity() as MainActions).showSnackBar(
            if (holiday.isLike)
                R.string.snack_add_to_favorite
            else
                R.string.snack_remove_from_favorite
        )

        if (holiday.isLike)
            holidayViewModel.addHolidayLike(holiday.id)
        else
            holidayViewModel.removeFavorite(holiday.id)
    }

    private fun loadHolidaysOfDay() {
        holidayViewModel.loadHolidaysOfDay(Utils.convertDateToDateString(mainViewModel.currentDate))
    }

    private fun initObservers() {
        observeLoadHolidaysOfDay()
        observeLoadNextDayWithHolidays()
        observeLoadNextDateWithHolidays()
    }

    private fun setDate(calendar: Calendar) {
        with(calendar) {
            val day = time.withPattern(Constants.PATTERN_D)
            val month = time.withPattern(Constants.PATTERN_MMMM)
            binding.tvPostInfo.text = "$day $month"
        }
    }

    private fun observeLoadHolidaysOfDay() {
        holidayViewModel.holidaysOfDayResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    setDate(Calendar.getInstance().apply { time = mainViewModel.currentDate })

                    showShimmers()
                    hideContent()
                    binding.ivCalendar.isEnabled = false
                }
                Status.SUCCESS -> {
                    it.data?.let { holidays ->
                        if (holidays.isEmpty())
                            mainViewModel.currentDate.let { date ->
                                holidayViewModel.loadNextDateWithHolidays(
                                    Utils.convertDateToDateString(
                                        date
                                    )
                                )
                            }
                        else {
                            setContent(holidays = holidays)
                            hideShimmers()
                            binding.ivCalendar.isEnabled = true
                        }
                    } ?: run {
                        hideShimmers()
                        binding.ivCalendar.isEnabled = true
                        showInfoBoardSupportError()
                    }
                }
                Status.ERROR -> {
                    hideShimmers()
                    binding.ivCalendar.isEnabled = true
                    when (it.throwable) {
                        is IllegalArgumentException, is JsonDataException -> showInfoBoardSupportError()
                        else -> showInfoBoardInternetError()
                    }
                }
            }
        }
    }

    private fun showInfoBoardSupportError() {
        showInfoBoard(
            getString(R.string.error_internet),
            getString(R.string.error_tech_support_description),
            R.drawable.ic_alien, R.string.button_try_again
        )
    }

    private fun showInfoBoardInternetError() {
        showInfoBoard(
            getString(R.string.error_internet),
            getString(R.string.error_internet_description),
            R.drawable.ic_alien, R.string.button_try_again
        )
    }

    private fun showInfoBoard(
        title: String,
        text: String,
        @DrawableRes icon: Int? = null,
        @StringRes titleButton: Int? = null
    ) {
        with(binding.infoBoardView) {
            setContent(title, text, icon, titleButton)
            show()
        }
    }

    private fun showShimmers() {
        binding.layoutHolidayShimmer.root.show()
        binding.layoutHolidayShortShimmer.root.show()
    }

    private fun hideShimmers() {
        binding.layoutHolidayShimmer.root.gone()
        binding.layoutHolidayShortShimmer.root.gone()
    }

    private fun hideContent() {
        with(binding) {
            holidayView.gone()
            cvAnotherHolidays.gone()
            mbGoBack.gone()
            infoBoardView.gone()
            holidaysOfCurrentDayEmptyView.gone()
        }
    }

    private fun setContent(holidays: List<HolidayModel>, date: String? = null) {
        with(binding) {
            if (holidays.isEmpty()) {
                date?.let { holidaysOfCurrentDayEmptyView.initDate(date = it) }
                holidaysOfCurrentDayEmptyView.show()
            } else {
                with(holidayView) {
                    show()
                    initHoliday(holiday = holidays.first())
                }
                if (holidays.size > 1) {
                    cvAnotherHolidays.show()
                    initHolidaysShortAdapter(holidays = holidays.filterIndexed { index, _ -> index != 0 })
                }
                binding.mbGoBack.isVisible =
                    mainViewModel.currentDate.withPattern(Constants.DEFAULT_DATE_PATTERN) != Calendar.getInstance().time.withPattern(
                        Constants.DEFAULT_DATE_PATTERN
                    )
            }
        }
    }

    private fun observeLoadNextDayWithHolidays() {
        holidayViewModel.nextDateWithHolidaysResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    setDate(Calendar.getInstance().apply { time = mainViewModel.currentDate })
                }
                Status.SUCCESS -> {
                    binding.ivCalendar.isEnabled = true
                    hideShimmers()

                    it.data?.let { data ->
                        data.dateString?.let {
                            binding.holidaysOfCurrentDayEmptyView.show()
                            binding.holidaysOfCurrentDayEmptyView.initDate(date = it)
                        } ?: run {
                            showInfoBoardSupportError()
                        }
                    } ?: run {
                        showInfoBoardSupportError()
                    }
                }
                Status.ERROR -> {
                    binding.ivCalendar.isEnabled = true
                    hideShimmers()
                    showInfoBoardInternetError()
                }
            }
        }
    }

    private fun observeLoadNextDateWithHolidays() {
        holidayViewModel.nextDateWithHolidaysResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    setDate(Calendar.getInstance().apply { time = mainViewModel.currentDate })
                }
                Status.SUCCESS -> {
                    it.data?.dateString?.let { date ->
                        holidayViewModel.nextDayWithHolidays = date
                    }
                    WorkerModule.runHolidayWorker(requireContext())
                }
                Status.ERROR -> {

                }
            }
        }
    }

    private fun setCalendarClick() {
        binding.ivCalendar.setOnClickListener {
            CalendarBottomFragment.getInstance(mainViewModel.currentDate.time)
                .show(childFragmentManager, CalendarBottomFragment.javaClass.name)
        }
    }

    private fun initHolidaysShortAdapter(holidays: List<HolidayModel>) {
        with(binding.rvAnotherHolidays) {
            if (adapter == null)
                adapter = HolidayShortAdapter(
                    holidays.toMutableList(),
                    ::openClick,
                    ::changeLike,
                    ::openLongClick,
                    ::onPhotoClick
                )
            else
                (adapter as AdapterActions).setData(holidays)
        }
    }

    private fun onPhotoClick(holiday: HolidayModel, photo: PhotoModel) {
        holiday.images?.let {
            onOpen(
                PhotoFragment.getInstance(photos = it.toTypedArray(), position = it.indexOf(photo)),
                isAdd = true
            )
        }
    }

    private fun openClick(holiday: HolidayModel) {
        onOpen(HolidayFragment.getInstance(holiday = holiday))
    }

    private fun openLongClick(holidayResponse: HolidayModel) {

    }

    companion object {
        fun getInstance() = MainFragment().apply { }
    }
}