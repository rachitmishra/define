package `in`.ceeq.define.ui

import `in`.ceeq.define.R
import `in`.ceeq.define.databinding.ActivityMainBinding
import `in`.ceeq.define.ui.viewmodel.DefinitionViewModel
import `in`.ceeq.define.utils.isNetConnected
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mDefinitionViewModel: DefinitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding.dvm = mDefinitionViewModel

        title = ""

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)

        if (null != text) {
            mDefinitionViewModel.phrase = text.toString()
        }

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
            handleCopyDefinition(mDefinitionViewModel.definition.getDefinition())
            true
        }
        txtDefinition.setOnLongClickListener {
            handleCopyDefinition(mDefinitionViewModel.definition.getAlternateDefinition())
            true
        }
    }

    private fun onClose() {
        finish()
    }

    private fun handleLoadSuggestionClick() {
        mDefinitionViewModel.loadSuggestedPhrase()
    }

    private fun handleOpenSettings() {
        SettingsActivity.start(this)
    }

    private fun handleShareDefinition() {
        // TODO
    }

    private fun handleCopyDefinition(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = ClipData.newPlainText(mDefinitionViewModel.phrase, text)
        Toast.makeText(this, R.string.definition_copied, Toast.LENGTH_SHORT).show()
    }

    private fun loadDefinition() {
        if (isNetConnected(this)) {
            mDefinitionViewModel.loadDefinition()
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
