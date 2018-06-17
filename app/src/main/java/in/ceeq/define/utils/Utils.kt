package `in`.ceeq.define.utils

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned

fun AppCompatActivity.isNetConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return !(networkInfo == null || !networkInfo.isConnectedOrConnecting)
}

fun fromHtml(html: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}

infix fun <T : ViewDataBinding> Activity.setDataBindingView(layout: Int): T =
        DataBindingUtil.setContentView(this, layout)
