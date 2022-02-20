package ru.createtogether.feature_worker_impl

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.createtogether.feature_worker_impl.test", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWork(context: Context) {

        // Create periodic work request
        val request: PeriodicWorkRequest = PeriodicWorkRequest.Builder(HolidayWorker::class.java, 15, TimeUnit.MINUTES)
            .build()
        // Enqueue periodic request
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(HolidayWorker.TAG, ExistingPeriodicWorkPolicy.REPLACE, request)

        // Initialize testDriver
        val testDriver = WorkManagerTestInitHelper.getTestDriver()

        // Tells the testing framework the period delay is met, this will execute your code in doWork() in MyWorker class
        testDriver!!.setPeriodDelayMet(request.getId())
    }
}