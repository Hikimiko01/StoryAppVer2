package com.dicoding.picodiploma.storyapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.storyapp.view.login.LoginActivity
import com.dicoding.storyapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginLogoutTest {

    // Rule to start the LoginActivity
    @get:Rule
    var loginActivityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginAndLogoutFlow() {
        // Mencoba login dengan data yang valid
        performLogin("admin@gmail.com", "12345678")

        // Memastikan MainActivity sudah dijalankan
        onView(withId(R.id.greeting_tv)).check(matches(isDisplayed()))

        // Melakukan log out
        onView(withId(R.id.action_logout)).perform(click())

        // Memastikan kembali ke WelcomeActivity setelah log out
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginWithInvalidCredentials() {
        // Memasukkan data yang invalid
        onView(withId(R.id.ed_login_email))
            .perform(typeText("wronguser@example.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(typeText("wrongpassword"), closeSoftKeyboard())

        // Melakukan click pada login button
        onView(withId(R.id.loginButton)).perform(click())

        // Mengecek apakah progressbar tampil
        onView(withId(R.id.loadingProgressBar)).check(matches(isDisplayed()))

        // Memastikan error dialog
        onView(withText(R.string.acc_not_found))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // Klik SignUp Button pada dialog
        onView(withText(R.string.daftar))
            .inRoot(isDialog())
            .perform(click())

        // Memastikan bahwa layout SignUp yang ditampilkan
        onView(withId(R.id.signupButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyFieldsLoginAttempt() {
        // Klik tombol login tanpa mengisi data
        onView(withId(R.id.loginButton)).perform(click())

        // Memastikan error dialog tampil
        onView(withText(R.string.field_not_filled))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // Klik tombol kembali pada dialog
        onView(withText(R.string.back))
            .inRoot(isDialog())
            .perform(click())
    }

    // Fungsi bantuan untuk login
    private fun performLogin(email: String, password: String) {
        // Memasukkan email dan password
        onView(withId(R.id.ed_login_email))
            .perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(typeText(password), closeSoftKeyboard())

        // Klik tombol login
        onView(withId(R.id.loginButton)).perform(click())

        // Memastikan apabila loading tampil
        onView(withId(R.id.loadingProgressBar)).check(matches(isDisplayed()))

        // Memastikan dialog sukses tampil
        onView(withText(R.string.login_success))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // Klik "Lanjut" pada dialog
        onView(withText(R.string.lanjut)).perform(click())
    }
}