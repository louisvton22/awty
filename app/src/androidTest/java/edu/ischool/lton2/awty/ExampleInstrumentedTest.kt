package edu.ischool.lton2.awty

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*



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
        assertEquals("edu.ischool.lton2.awty", appContext.packageName)
    }

    @Test
    fun phoneNumberCorrect() {
        val phoneNumber = "2066737818"
        assertEquals("(206) 673-7818", formatPhone(phoneNumber),)
    }
}