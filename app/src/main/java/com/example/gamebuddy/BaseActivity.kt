package com.example.gamebuddy

import android.content.pm.PackageManager
import android.view.ContextThemeWrapper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.example.gamebuddy.presentation.UICommunicationListener
import com.example.gamebuddy.util.Constants.PERMISSIONS_REQUEST_READ_STORAGE

abstract class BaseActivity : AppCompatActivity(), UICommunicationListener {

    private var dialogInView: MaterialDialog? = null

    override fun displayProgressBar(isLoading: Boolean) {
        if (currentFocus != null) {
            hideSoftKeyboard()
            val inputMethodManager =
                getSystemService(ContextThemeWrapper.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(ContextThemeWrapper.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    /**
     * This code is checking if the app has been granted permission to access the device's external storage for reading and writing.
     * If the permissions have not been granted, the code requests them from the user.
     * The isStoragePermissionGranted() function returns a Boolean value indicating whether or not the permissions have been granted.
     * If the permissions have been granted, the function returns true. If the permissions have not been granted, the function requests the permissions and returns false.
     * It's worth noting that the code is using the Android Support Library's ContextCompat and ActivityCompat classes to check and request permissions, respectively.
     * This is done to ensure backwards compatibility with older Android versions.
     * */

    override fun isStoragePermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_READ_STORAGE
            )

            return false
        } else {
            // Permission has already been granted
            return true
        }
    }

    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

}
