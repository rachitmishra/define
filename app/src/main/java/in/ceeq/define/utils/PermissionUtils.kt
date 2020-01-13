package `in`.ceeq.define.utils

import `in`.ceeq.define.R
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object PermissionUtils {

    private const val REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 2
    private const val REQUEST_CODE_SETTINGS = 3

    /**
     * Return true if the permission available, else starts permission request from user
     *
     * @param activity   Source Activity
     * @param permission Permission for which Snackbar has to be shown,
     * helps in deciding the message string for Snackbar
     */
    fun requestPermission(activity: Activity,
                          permission: String): Boolean {

        return if (checkSelfPermission(activity, permission)) {
            true
        } else {
            val requestCode = getRequestCodeByPermission(permission)

            ActivityCompat.requestPermissions(activity,
                    arrayOf(permission),
                    requestCode)
            false
        }
    }

    fun requestStoragePermission(activity: Activity): Boolean {
        return requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * Return true if the permission available, else starts permission request from user
     *
     * @param fragment   Source Fragment
     * @param permission Permission for which Snackbar has to be shown,
     * helps in deciding the message string for Snackbar
     */
    fun requestPermission(fragment: Fragment,
                          permission: String): Boolean {
        return if (checkSelfPermission(fragment.activity!!, permission)) {
            true
        } else {
            val requestCode = getRequestCodeByPermission(permission)

            fragment.requestPermissions(
                    arrayOf(permission),
                    requestCode)
            false
        }
    }

    fun checkStoragePermissionGranted(requestCode: Int, grantResults: IntArray): Boolean {
        return (requestCode == REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }


    private fun checkSelfPermission(activity: Activity,
                                    permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @param permission Permission for which Snackbar has to be shown,
     * helps in deciding the message string for Snackbar
     */
    private fun getRequestCodeByPermission(permission: String): Int {
        return REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE
    }

    /**
     * @param permission Permission for which Snackbar has to be shown,
     * helps in deciding the message string for Snackbar
     */
    private fun getPermissionDeclinedMessage(permission: String): Int {
        return when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> R.string.permission_denied_external_storage
            else -> R.string.permission_denied
        }
    }

    /**
     * @param activity Context where the Settings screen will open
     */
    private fun openSettingsScreen(activity: Activity, requestCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null) // NON-NLS
        intent.data = uri
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * @param activity   Context where the Snackbar will be shown
     * @param permission Permission for which Snackbar has to be shown,
     * helps in deciding the message string for Snackbar
     * @return snackbar snackbar instance which can be useful to set callbacks,if needed
     */
    fun showPermissionDeclineMessage(activity: Activity,
                                     permission: String): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
                getPermissionDeclinedMessage(permission), Snackbar.LENGTH_LONG)

        snackbar.setAction(R.string.settings) { openSettingsScreen(activity, REQUEST_CODE_SETTINGS) }
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent)).show()
        return snackbar
    }
}
