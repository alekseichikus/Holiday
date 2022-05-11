package ru.createtogether.fragment_favorite.presenter

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feature_adapter_generator.initAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.createtogether.common.helpers.Event
import ru.createtogether.common.helpers.Status
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.baseFragment.base.BaseDropDownListFragment
import ru.createtogether.common.helpers.extension.*
import ru.createtogether.feature_holiday.HolidayShortView
import ru.createtogether.feature_holiday_utils.helpers.HolidayShortAdapterListener
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

    private val holidayViewModel: BaseHolidayViewModel by viewModels()

    override val viewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        initListeners()
        initObservers()

        initRequests()
    }

    private fun initRequests() {
        loadHolidayByIds()
    }

    private fun loadHolidayByIds() {
        holidayViewModel.loadHolidaysById(holidayViewModel.getFavorites())
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
        lifecycleScope.launch {
            holidayViewModel.holidaysByIdResponse.collect {
                when (it) {
                    is Event.Loading -> {
                        with(binding) {
                            progressBar.show()
                            infoBoardView.gone()
                            rvHolidaysShort.gone()
                            ivSort.gone()
                        }
                    }
                    is Event.Success -> {
                        with(binding) {
                            progressBar.gone()
                            if (it.data.isEmpty()) {
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
                                initHolidaysAdapter(holidays = it.data)
                            }
                        }
                    }
                    is Event.Error -> {
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
    }

    private fun initHolidaysAdapter(holidays: List<HolidayModel>) {
        binding.rvHolidaysShort.initAdapter(
            holidays,
            HolidayShortView::class.java,
            object : HolidayShortAdapterListener {
                override fun onLikeClick(holiday: HolidayModel) {
                    holidayViewModel.setFavorite(holiday = holiday)

                    requireView().showSnackBar(
                        getString(
                            if (holiday.isLike)
                                R.string.snack_add_to_favorite
                            else
                                R.string.snack_remove_from_favorite
                        )
                    )
                }

                override fun onLongClick(holiday: HolidayModel) {
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
                            0 -> onOpen(HolidayFragment.getInstance(holiday = holiday))

                            1 -> {
                                holidayViewModel.removeFavorite(holiday.id)
                                //holidayViewModel.holidaysByIdResponse.value = Event.success(holidayViewModel.holidaysByIdResponse.value?.data?.filter { it.id != holiday.id })
                            }
                        }
                    }.show(childFragmentManager, null)
                }

                override fun onPhotoClick(holiday: HolidayModel, photo: PhotoModel) {
                    onOpen(
                        PhotoFragment.getInstance(
                            photos = holiday.images,
                            position = holiday.images.indexOf(photo)
                        ),
                        isAdd = true
                    )
                }

                override fun onClick(item: HolidayModel) {
                    onOpen(HolidayFragment.getInstance(holiday = item))
                }
            },
            object :
                com.example.feature_adapter_generator.DiffUtilTheSameCallback<HolidayModel> {
                override fun areItemsTheSame(oldItem: HolidayModel, newItem: HolidayModel) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: HolidayModel, newItem: HolidayModel) =
                    oldItem.isLike == newItem.isLike
            }
        )
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
            holidayViewModel.holidaysByIdResponse.value.let { state ->
                if (state is Event.Success) {
                    holidayViewModel.setHolidaysById(
                        holidays = viewModel.updateSort(
                            it.itemId,
                            state.data
                        )
                    )
                }
            }
            true
        }
    }

    companion object {
        fun getInstance() = FavoriteFragment().apply { }
    }
}