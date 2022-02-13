package ru.createtogether.holiday

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.databinding.ActivityMainBinding
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.extension.onOpen
import ru.createtogether.fragment_main.presenter.MainFragment
import ru.createtogether.fragment_main.presenter.viewModel.MainViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActions {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initData()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureViews()

        onOpen(MainFragment.getInstance())
    }

    private fun configureViews() {
        hideSystemUI()
    }

    private fun initData(){
        mainViewModel.versionCode = BuildConfig.VERSION_NAME
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            binding.root.setOnApplyWindowInsetsListener { v, insets ->

                v.updatePadding(
                    bottom = insets.getInsets(WindowInsets.Type.systemBars()).bottom,
                    right = insets.getInsets(WindowInsets.Type.systemBars()).right,
                    left = insets.getInsets(WindowInsets.Type.systemBars()).left,
                )
                return@setOnApplyWindowInsetsListener insets
            }
        }
    }

    override fun changeNavigationBarColor(colorRes: Int) {
        window.navigationBarColor = ContextCompat.getColor(this, colorRes)
    }

    override fun showSnackBar(stringRes: Int) {
        Snackbar.make(binding.root, stringRes, Snackbar.LENGTH_SHORT).show()
    }
}