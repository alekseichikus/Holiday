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
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import com.example.feature_adapter_generator.initAdapter
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_holiday.HolidayShortView
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.feature_holiday_utils.helpers.HolidayUtil
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_info_board.helpers.InfoBoardListener
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
    private val holidayViewModel: BaseHolidayViewModel by viewModels()
    override val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()

        configureViews()
        initListeners()
        initObservers()

        initRequests()
    }

    private fun initRequests() {
        if (holidayViewModel.holidaysOfDayResponse.value.status == Status.LOADING) {
            loadHolidaysOfDay()
        }
    }

    override fun initDataBinding() {
        binding.mainFragment = this
    }

    override fun configureViews() {
        binding.clScrollContainer.setPaddingTop()
    }

    override fun initListeners() {
        setCalendarResult()
        setHolidayViewListener()
        setInfoBoardViewListener()
        setHolidaysOfCurrentDayEmptyListener()
    }

    override fun onGoBackClick() {
        viewModel.setDate(Calendar.getInstance().time)
        loadHolidaysOfDay()
    }

    override fun onMenuClick() {
        onOpen(AboutFragment.getInstance())
    }

    override fun onSearchClick() {
        (requireActivity() as MainActions).showSnackBar(R.string.coming_soon)
    }

    override fun setHolidaysOfCurrentDayEmptyListener() {
        binding.holidaysOfCurrentDayEmptyView.setGoToClickListener { date ->
            viewModel.setDate(date = date)
            loadHolidaysOfDay()
        }
    }

    override fun setInfoBoardViewListener() {
        binding.infoBoardView.setInfoBoardListener(object : InfoBoardListener {
            override fun onActionClick() {
                loadHolidaysOfDay()
            }
        })
    }

    override fun setHolidayViewListener() {
        binding.holidayView.setLikeClickListener { holiday ->
            holidayViewModel.setFavorite(holiday = holiday)
            binding.invalidateAll()
            requireView().showSnackBar(
                getString(
                    if (holiday.isLike)
                        R.string.snack_add_to_favorite
                    else
                        R.string.snack_remove_from_favorite
                )
            )
        }
    }

    override fun onFavoriteClick() {
        onOpen(FavoriteFragment.getInstance())
    }

    override fun onHolidayClick() {
        onOpen(HolidayFragment.getInstance(holiday = binding.holidayView.holiday))
    }

    override fun onCalendarClick() {
        CalendarBottomFragment.getInstance(viewModel.getDate().time)
            .show(childFragmentManager, CalendarBottomFragment.javaClass.name)
    }

    override fun setCalendarResult() {
        childFragmentManager.setFragmentResultListener(
            CalendarBottomFragment.CALENDAR_REQUEST, viewLifecycleOwner
        ) { _, result ->
            result.getLong(CalendarBottomFragment.DATE_LONG).let { timeInMillis ->
                viewModel.setDate(Date().apply { time = timeInMillis })
                loadHolidaysOfDay()
            }
        }
    }

    override fun initObservers() {
        observeLoadHolidaysOfDay()
        observeLoadNextDayWithHolidays()
    }

    override fun observeLoadHolidaysOfDay() {
        observeStateFlow(holidayViewModel.holidaysOfDayResponse, onLoading = {
            setDate(viewModel.getDate())

            showShimmers()
            blockUI()
            hideContent()
        }, onSuccess = { holidays ->
            if (holidays.isEmpty()) {
                holidayViewModel.loadNextDateWithHolidays(date = viewModel.getDate())
            } else {
                hideShimmers()
                unBlockUI()
                setContent(holidays = holidays)
            }
        }, onError = { throwable ->
            hideShimmers()
            unBlockUI()
            when (throwable) {
                is IllegalArgumentException, is JsonDataException -> showInfoBoardSupportError()
                else -> showInfoBoardInternetError()
            }
        })
    }

    override fun observeLoadNextDayWithHolidays() {
        observeStateFlow(holidayViewModel.nextDateWithHolidaysResponse,
            onSuccess = { day ->
                unBlockUI()
                hideShimmers()

                binding.holidaysOfCurrentDayEmptyView.show()
                binding.holidaysOfCurrentDayEmptyView.initDate(
                    date = Calendar.getInstance().setDateString(day.dateString).time
                )
            }, onError = { throwable ->
                unBlockUI()
                hideShimmers()
                when (throwable) {
                    is IllegalArgumentException, is JsonDataException -> showInfoBoardSupportError()
                    else -> showInfoBoardInternetError()
                }
            })
    }

    private fun loadHolidaysOfDay() {
        holidayViewModel.loadHolidaysOfDay(viewModel.getDate())
    }

    override fun setDate(date: Date) {
        val day = date.withPattern(Constants.PATTERN_D)
        val month = date.withPattern(Constants.PATTERN_MMMM)
        binding.tvPostInfo.text = "$day $month"
    }

    override fun blockUI() {
        binding.ivCalendar.isEnabled = false
    }

    override fun unBlockUI() {
        binding.ivCalendar.isEnabled = true
    }

    override fun showInfoBoardSupportError() {
        showInfoBoard(
            getString(R.string.error_internet),
            getString(R.string.error_tech_support_description),
            R.drawable.ic_alien, R.string.button_try_again
        )
    }

    override fun showInfoBoardInternetError() {
        showInfoBoard(
            getString(R.string.error_internet),
            getString(R.string.error_internet_description),
            R.drawable.ic_alien, R.string.button_try_again
        )
    }

    override fun showInfoBoard(
        title: String,
        text: String,
        @DrawableRes icon: Int?,
        @StringRes titleButton: Int?
    ) {
        with(binding.infoBoardView) {
            setContent(title, text, icon, titleButton)
            show()
        }
    }

    override fun showShimmers() {
        binding.layoutShimmers.show()
    }

    override fun hideShimmers() {
        binding.layoutShimmers.gone()
    }

    override fun hideContent() {
        with(binding) {
            holidayView.gone()
            cvAnotherHolidays.gone()
            mbGoBack.gone()
            infoBoardView.gone()
            holidaysOfCurrentDayEmptyView.gone()
        }
    }

    override fun setContent(holidays: List<HolidayModel>, date: Date?) {
        if (holidays.isEmpty()) {
            date?.let { updateHolidaysOfCurrentDayEmptyView(it) }
        } else {
            updateHolidayView(holiday = holidays.first())
            updateHolidayShortView(holidays = holidays)
            binding.mbGoBack.isVisible =
                viewModel.getDate().compareTo(Calendar.getInstance().time) == 0
        }
    }

    private fun updateHolidaysOfCurrentDayEmptyView(date: Date) {
        date.let { binding.holidaysOfCurrentDayEmptyView.initDate(date = it) }
        binding.holidaysOfCurrentDayEmptyView.isVisible = false
    }

    private fun updateHolidayView(holiday: HolidayModel) {
        binding.holidayView.show()
        binding.holidayView.initHoliday(holiday = holiday)
    }

    private fun updateHolidayShortView(holidays: List<HolidayModel>) {
        with(holidays.size > 1) {
            binding.cvAnotherHolidays.isVisible = this
            if (this)
                initHolidaysShortAdapter(holidays = holidays.filterIndexed { index, _ -> index != 0 })
        }
    }

    override fun initHolidaysShortAdapter(holidays: List<HolidayModel>) {
        binding.rvAnotherHolidays.initAdapter(
            holidays,
            HolidayShortView::class.java,
            HolidayUtil.getHolidayShortAdapterListener(
                onLikeClick = { holiday ->
                    holidayViewModel.setFavorite(holiday = holiday)
                    requireView().showSnackBar(
                        getString(
                            if (holiday.isLike)
                                R.string.snack_add_to_favorite
                            else
                                R.string.snack_remove_from_favorite
                        )
                    )
                }, onPhotoClick = { holiday, photo ->
                    onOpen(
                        PhotoFragment.getInstance(
                            photos = holiday.images,
                            position = holiday.images.indexOf(photo)
                        ),
                        isAdd = true
                    )
                }, onClick = { holiday ->
                    onOpen(HolidayFragment.getInstance(holiday = holiday))
                }
            ),
            HolidayUtil.getHolidayBaseDiffUtilTheSameCallback()
        )
    }

    companion object {
        fun getInstance() = MainFragment().apply { }
    }
}