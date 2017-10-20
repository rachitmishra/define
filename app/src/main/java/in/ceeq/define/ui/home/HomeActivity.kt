package `in`.ceeq.define.ui.main

import `in`.ceeq.define.R
import `in`.ceeq.define.databinding.ActivityHomeBinding
import `in`.ceeq.define.ui.home.HomeViewModel
import `in`.ceeq.define.ui.settings.SettingsActivity
import `in`.ceeq.define.utils.isNetConnected
import `in`.ceeq.define.utils.setDataBindingView
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val activityHomeBinding = setDataBindingView<ActivityHomeBinding>(R.layout.activity_home)
        activityHomeBinding.viewModel = homeViewModel

        title = ""

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)

        if (null != text) {
            homeViewModel.phrase = text.toString()
        }

        initHandlers()
        loadDefinition()
    }

    private fun initHandlers() {
        btnSettings.setOnClickListener {
            handleOpenSettings()
        }
        btnClose1.setOnClickListener {
            onClose()
        }
        btnClose2.setOnClickListener {
            onClose()
        }
        btnSuggestedPhrase.setOnClickListener {
            handleLoadSuggestionClick()
        }
        txtDefinition.setOnLongClickListener {
            handleCopyDefinition(homeViewModel.definition.getDefinition())
            true
        }
        txtDefinition.setOnLongClickListener {
            handleCopyDefinition(homeViewModel.definition.getAlternateDefinition())
            true
        }
    }

    private fun onClose() {
        finish()
    }

    private fun handleLoadSuggestionClick() {
        homeViewModel.loadSuggestedPhrase()
    }

    private fun handleOpenSettings() {
        SettingsActivity.start(this)
    }

    private fun handleShareDefinition() {
        // TODO
    }

    private fun handleCopyDefinition(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = ClipData.newPlainText(homeViewModel.phrase, text)
        Toast.makeText(this, R.string.definition_copied, Toast.LENGTH_SHORT).show()
    }

    private fun loadDefinition() {
        if (isNetConnected(this)) {
            homeViewModel.loadDefinition()
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
