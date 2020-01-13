package `in`.ceeq.define.ui.settings

import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.R
import `in`.ceeq.define.databinding.ActivitySettingsBinding
import `in`.ceeq.define.utils.setDataBindingView
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var mSettingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val activitySettingsBinding = setDataBindingView<ActivitySettingsBinding>(R.layout.activity_settings)
        activitySettingsBinding.settingsViewModel = mSettingsViewModel

        title = ""

        initHandlers()
    }

    private fun initHandlers() {
        btnRateApp.setOnClickListener {
            handleRateApp()
        }
        btnClose.setOnClickListener {
            onBackPressed()
        }
        btnFeedback.setOnClickListener {
            handleProvideFeedback()
        }
        btnShareApp.setOnClickListener {
            handleShareApp()
        }
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onLanguageSelected(position)
            }
        }
    }

    private fun handleRateApp() {
        val appPackageName = BuildConfig.APPLICATION_ID
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_MARKET_URI + appPackageName)))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_APP_DETAILS_URI + appPackageName)))
        }

    }

    private fun handleShareApp() {
        val shareString = resources.getString(R.string.share_message)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
    }

    private fun onLanguageSelected(position: Int) {
        mSettingsViewModel.saveSelectedLanguagePosition(position)
    }

    private fun handleProvideFeedback() {
        var debugInfo = BuildConfig.VERSION_NAME
        debugInfo += "_" + BuildConfig.VERSION_CODE
        debugInfo += "_" + Build.VERSION.SDK_INT
        debugInfo += "_" + Build.VERSION.RELEASE
        debugInfo += "_" + Build.MANUFACTURER
        debugInfo += "_" + Build.MODEL

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_EMAIL, "support@ceeq.in")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Define")
        shareIntent.putExtra(Intent.EXTRA_TEXT, debugInfo)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, getString(R.string.send_email)))

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {

        private val PLAY_STORE_APP_DETAILS_URI = "https://play.google.com/store/apps/details?id="
        private val PLAY_STORE_APP_MARKET_URI = "market://details?id="

        fun start(context: Context) {
            val starter = Intent(context, SettingsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
