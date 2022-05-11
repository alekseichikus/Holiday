package ru.createtogether.feature_photo

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ru.createtogether.feature_photo_utils.PhotoModel

class MockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(LinearLayout(this)) {
            addView(PhotoSmallView(this@MockActivity).apply {
                initData(photoModel!!) {

                }
            })
            setContentView(this)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object {
        var photoModel: PhotoModel? = null
    }
}
