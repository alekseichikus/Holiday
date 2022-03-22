package ru.createtogether.feature_holiday

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.createtogether.common.helpers.extension.setDateString
import ru.createtogether.common.helpers.extension.withPattern
import ru.createtogether.feature_holiday.databinding.ViewHolidaysOfCurrentDayEmptyBinding
import java.util.*

class HolidaysOfCurrentDayEmptyView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    private var _binding: ViewHolidaysOfCurrentDayEmptyBinding? = null
    private val binding get() = _binding!!

    private lateinit var goToClick: (date: Date) -> Unit
    private lateinit var date: Date

    init {
        _binding =
            ViewHolidaysOfCurrentDayEmptyBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        initView()
    }

    fun setGoToClickListener(goToClick: (date: Date) -> Unit){
        this.goToClick = goToClick
    }

    fun initDate(date: Date) {
        this.date = date

        with(binding) {

            val day = date.withPattern(Constants.PATTERN_D)
            val month = date.withPattern(Constants.PATTERN_MMMM)

            tvHolidayEmptyDate.text = "$day\n$month"
            mbMoveToDate.text = context.getString(R.string.go_to, "$day $month")
        }
    }

    private fun initView() {
        initListeners()
    }

    private fun initListeners() {
        goToClick()
    }

    private fun goToClick(){
        binding.mbMoveToDate.setOnClickListener {
            goToClick(date)
        }
    }
}