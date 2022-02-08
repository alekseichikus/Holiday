package ru.createtogether.holiday

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import ru.createtogether.common.databinding.ActivityMainBinding
import ru.createtogether.common.helpers.MainActions
import ru.createtogether.common.helpers.baseFragment.extension.onOpen
import ru.createtogether.fragment_main.viewModel.MainFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActions {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureViews()

        onOpen(MainFragment.getInstance())
    }

    private fun configureViews() {
        hideSystemUI()
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
}