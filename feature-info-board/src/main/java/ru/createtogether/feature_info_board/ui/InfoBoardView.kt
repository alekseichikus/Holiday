package ru.createtogether.feature_info_board.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import ru.createtogether.feature_info_board.R
import ru.createtogether.feature_info_board.databinding.LayoutInfoBoardBinding
import ru.createtogether.feature_info_board.helpers.InfoBoardListener

class InfoBoardView(context: Context, var attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var _binding: LayoutInfoBoardBinding? = null
    private val binding get() = _binding!!

    private var infoBoardListener: InfoBoardListener? = null

    init {
        _binding = LayoutInfoBoardBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        initViews()
        initListeners()
    }

    private fun initListeners(){
        setActionClick()
    }

    private fun setActionClick(){
        binding.mbAction.setOnClickListener {
            getInfoBoardListener().onActionClick()
        }
    }

    private fun initViews() {
        if (attrs != null) {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.InfoBoardView, 0, 0)
            setTitle(typedArray.getString(R.styleable.InfoBoardView_ibvTitle))
            setText(typedArray.getString(R.styleable.InfoBoardView_ibvText))
            setIcon(typedArray.getResourceId(
                R.styleable.InfoBoardView_ibvImage,
                R.drawable.ic_heart
            ))
            typedArray.recycle()
        }
    }

    private fun setTitle(text: String?) {
        binding.tvTitle.isVisible = text != null
        binding.tvTitle.text = text
    }

    private fun setText(text: String?) {
        binding.tvText.isVisible = text != null
        binding.tvText.text = text
    }

    private fun setIcon(@DrawableRes icon: Int?) {
        icon?.let {
            binding.ivPreview.setImageResource(it)
        }
    }

    fun setContent(title: String, text: String, @DrawableRes icon: Int? = null, @StringRes titleButton: Int? = null){
        setTitle(title)
        setText(text)
        setIcon(icon)
        setActionButton(titleButton)
    }

    private fun setActionButton(@StringRes idText: Int?){
        binding.mbAction.isVisible = (idText == null).not()
        idText?.let {
            binding.mbAction.text = context.getString(idText)
        }
    }

    private fun getInfoBoardListener() =
        infoBoardListener ?: throw IllegalArgumentException("::InfoBoardView Required InfoBoardListener")

    fun setInfoBoardListener(infoBoardListener: InfoBoardListener) {
        this.infoBoardListener = infoBoardListener
    }
}