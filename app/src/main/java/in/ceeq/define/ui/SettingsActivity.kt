package `in`.ceeq.define.ui

import `in`.ceeq.define.R
import `in`.ceeq.define.databinding.ActivitySettingsBinding
import `in`.ceeq.define.ui.viewmodel.SettingsViewModel
import `in`.ceeq.define.utils.PreferenceUtils
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private var mSettingsViewModel: SettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (null == mSettingsViewModel) {
            mSettingsViewModel = SettingsViewModel(resources, PreferenceUtils.newInstance(this))
        }

        val activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        activitySettingsBinding.settingsViewModel = mSettingsViewModel

        title = ""
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, SettingsActivity::class.java)
            context.startActivity(starter)
        }
    }
}
