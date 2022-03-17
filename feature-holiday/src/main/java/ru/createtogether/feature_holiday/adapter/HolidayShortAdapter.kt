package ru.createtogether.feature_holiday.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.createtogether.common.helpers.AdapterActions
import ru.createtogether.common.helpers.baseFragment.base.adapter.BaseAction
import ru.createtogether.feature_holiday.HolidayShortView
import ru.createtogether.feature_holiday_utils.helpers.HolidayShortDiffUtilCallback
import ru.createtogether.feature_holiday_utils.model.HolidayModel
import ru.createtogether.feature_photo_utils.PhotoModel

class HolidayShortAdapter(
    private var holidays: MutableList<HolidayModel>,
    private val onOpenClick: HolidayModel.() -> Unit,
    private val onChangeLike: HolidayModel.() -> Unit,
    private val onLongClick: HolidayModel.() -> Unit,
    private val onPhotoClick: (HolidayModel, PhotoModel) -> Unit,
    private val isFavoriteShow: Boolean = true
) : RecyclerView.Adapter<HolidayShortAdapter.HolidayShortViewHolder>(), AdapterActions<HolidayModel> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HolidayShortViewHolder(
            HolidayShortView(
                parent.context,
                onChangeLike,
                onOpenClick,
                onLongClick,
                onPhotoClick,
                isFavoriteShow
            )
        )

    override fun onBindViewHolder(holder: HolidayShortViewHolder, position: Int) {
        holder.bind(holidays[position])
    }

    override fun getItemCount() = holidays.size

    inner class HolidayShortViewHolder(
        private val holidayShortView: HolidayShortView
    ) : RecyclerView.ViewHolder(holidayShortView) {
        fun bind(holiday: HolidayModel) {
            holidayShortView.setHoliday(holiday = holiday)
        }
    }

    override var items: Array<HolidayModel> = emptyArray()
    override lateinit var action: BaseAction<HolidayModel>

    override fun setData(array: Array<HolidayModel>) {
        val holidayShortDiffUtilCallback =
            HolidayShortDiffUtilCallback(getData(), array)
        val productDiffResult = DiffUtil.calculateDiff(holidayShortDiffUtilCallback)
        items = array
        productDiffResult.dispatchUpdatesTo(this)
    }

    override fun getData(): Array<HolidayModel> = items
}