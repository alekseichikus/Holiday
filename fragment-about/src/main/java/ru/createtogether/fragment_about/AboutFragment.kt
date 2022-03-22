package ru.createtogether.fragment_about

import Constants
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.helpers.baseFragment.BaseFragment
import ru.createtogether.common.helpers.extension.onBack
import ru.createtogether.common.helpers.extension.setPaddingTop
import ru.createtogether.feature_holiday_impl.viewModel.BaseHolidayViewModel
import ru.createtogether.fragment_about.databinding.FragmentAboutBinding
import ru.createtogether.fragment_about.presenter.viewModel.AboutViewModel


@AndroidEntryPoint
class AboutFragment : BaseFragment(R.layout.fragment_about) {
    private val binding: FragmentAboutBinding by viewBinding()

    override val viewModel: AboutViewModel by viewModels()
    private val baseHolidayViewModel: BaseHolidayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVersion()
        setHolidayNotifications()
        configureViews()
        initListeners()
    }

    private fun setHolidayNotifications(){
        binding.swHolidayNotifications.isChecked = baseHolidayViewModel.isNotifyAboutHolidays
    }

    private fun setVersion() {
        binding.tvVersion.text = getString(R.string.version_code, viewModel.versionCode)
    }

    private fun configureViews() {
        binding.root.setPaddingTop()
    }

    private fun initListeners() {
        setCloseClick()
        setMailClick()
        setRateApp()

        setNotifyAboutHolidaysCheckedChange()
    }

    private fun setNotifyAboutHolidaysCheckedChange(){
        binding.swHolidayNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            baseHolidayViewModel.isNotifyAboutHolidays = isChecked
        }
    }

    private fun setRateApp() {
        binding.mbRateApp.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${requireContext().packageName}")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                    )
                )
            }
        }
    }

    private fun setCloseClick() {
        binding.ivClose.setOnClickListener {
            onBack()
        }
    }

    private fun setMailClick() {
        binding.mbMail.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.FEEDBACK_MAIL))
        val chooser = Intent.createChooser(intent, getString(R.string.support))
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(chooser)
    }

    companion object {
        fun getInstance() = AboutFragment().apply { }
    }
}