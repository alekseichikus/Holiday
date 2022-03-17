package ru.createtogether.fragment_favorite.presenter

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.baseFragment.base.BaseDropDownListFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_holiday.adapter.HolidayShortAdapter
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_info_board.helpers.InfoBoardListener
import ru.createtogether.feature_photo_utils.PhotoModel
import ru.createtogether.fragment_favorite.R
import ru.createtogether.fragment_favorite.databinding.FragmentFavoriteBinding
import ru.createtogether.fragment_holiday.HolidayFragment
import ru.createtogether.fragment_photo.presenter.PhotoFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private val binding: FragmentFavoriteBinding by viewBinding()

    private val baseHolidayViewModel: BaseHolidayViewModel by viewModels()

    override val viewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        initListeners()
        initObservers()
        loadHolidayByIds()
    }

    private fun loadHolidayByIds() {
        baseHolidayViewModel.loadHolidaysById(baseHolidayViewModel.getFavorites())
    }

    private fun configureViews() {
        binding.llToolbar.setPaddingTop()
    }

    private fun initListeners() {
        setCloseClick()
        setTryAgainClick()
        setSortClick()
    }

    private fun setSortClick() {
        binding.ivSort.setOnClickListener {
            showSortPopup()
        }
    }

    private fun setTryAgainClick() {
        binding.infoBoardView.setInfoBoardListener(object : InfoBoardListener {
            override fun onActionClick() {
                loadHolidayByIds()
            }
        })
    }

    private fun initObservers() {
        observeLoadHolidayByIds()
    }

    private fun observeLoadHolidayByIds() {
        baseHolidayViewModel.holidaysByIdResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    with(binding) {
                        progressBar.show()
                        infoBoardView.gone()
                        rvHolidaysShort.gone()
                        ivSort.gone()
                    }
                }
                Status.SUCCESS -> {
                    with(binding) {
                        progressBar.gone()
                        if (it.data.isNullOrEmpty()) {
                            rvHolidaysShort.gone()
                            infoBoardView.show()
                            infoBoardView.setContent(
                                getString(R.string.title_favorites),
                                getString(R.string.empty_favorite_description),
                                R.drawable.ic_bookmark_r
                            )
                        } else {
                            rvHolidaysShort.show()
                            infoBoardView.gone()
                            ivSort.show()
                            initHolidaysAdapter(it.data!!)
                        }
                    }
                }
                Status.ERROR -> {
                    with(binding) {
                        progressBar.gone()
                        rvHolidaysShort.gone()
                        infoBoardView.show()
                        infoBoardView.setContent(
                            getString(R.string.error_internet),
                            getString(R.string.error_internet_description),
                            R.drawable.ic_alien, R.string.button_try_again
                        )
                    }
                }
            }
        }
    }

    private fun initHolidaysAdapter(holidays: List<HolidayModel>) {
        with(binding.rvHolidaysShort) {
            if (adapter == null)
                adapter = HolidayShortAdapter(
                    holidays.toMutableList(),
                    ::openClick,
                    ::changeLike,
                    ::openLongClick,
                    ::onPhotoClick,
                    isFavoriteShow = false
                )
        }
    }

    private fun openClick(holiday: HolidayModel) {
        onOpen(HolidayFragment.getInstance(holiday = holiday))
    }

    private fun onPhotoClick(holiday: HolidayModel, photo: PhotoModel) {
        holiday.images?.let {
            onOpen(
                PhotoFragment.getInstance(photos = it.toTypedArray(), position = it.indexOf(photo)),
                isAdd = true
            )
        }
    }

    private fun changeLike(holidayResponse: HolidayModel) {
        if (holidayResponse.isLike)
            baseHolidayViewModel.addHolidayLike(holidayResponse.id)
        else
            baseHolidayViewModel.removeFavorite(holidayResponse.id)
    }

    private fun openLongClick(holiday: HolidayModel) {
        BaseDropDownListFragment.getInstance(
            listOf(
                getString(
                    R.string.button_open
                ), getString(
                    R.string.button_remove
                )
            )
        ) { position ->
            when (position) {
                0 -> openClick(holiday = holiday)

                1 -> {
                    baseHolidayViewModel.removeFavorite(holiday.id)
                    baseHolidayViewModel.holidaysByIdResponse.postValue(Event.success(baseHolidayViewModel.holidaysByIdResponse.value?.data?.filter { it.id != holiday.id }))
                }
            }
        }.show(childFragmentManager, null)
    }

    private fun setCloseClick() {
        binding.ivClose.setOnClickListener {
            onBack()
        }
    }

    private fun showSortPopup() {
        val popupMenu = PopupMenu(requireContext(), binding.ivSort)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_sort, popupMenu.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.idName -> {
                    baseHolidayViewModel.holidaysByIdResponse.postValue(Event.success(baseHolidayViewModel.holidaysByIdResponse.value?.data?.sortedBy { it.title }))
                }
                R.id.idDate -> {
                    baseHolidayViewModel.holidaysByIdResponse.postValue(Event.success(baseHolidayViewModel.holidaysByIdResponse.value?.data?.sortedBy { it.date }))
                }
            }
            true
        }
    }

    companion object{
        fun getInstance() = FavoriteFragment().apply {  }
    }
}