package `in`.ceeq.define.ui.home

import `in`.ceeq.define.R
import `in`.ceeq.define.databinding.ActivityHomeBinding
import `in`.ceeq.define.ui.settings.SettingsActivity
import `in`.ceeq.define.utils.LogUtils
import `in`.ceeq.define.utils.PermissionUtils
import `in`.ceeq.define.utils.isNetConnected
import `in`.ceeq.define.utils.setDataBindingView
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private var cameraFileUrl: String? = null

    companion object {
        const val RC_CAMERA = 101
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = setDataBindingView(R.layout.activity_home)
        binding.viewModel = homeViewModel

        title = ""

        val phrase = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)

        initHandlers()

        loadDefinition(phrase?.toString() ?: "")
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

        definition.setOnLongClickListener {
            handleCopyDefinition(homeViewModel.definition.get())
            true
        }
        definition.setOnLongClickListener {
            handleCopyDefinition(homeViewModel.alternateDefinition.get())
            true
        }

        btnCamera.setOnClickListener {
            if (PermissionUtils.requestStoragePermission(this)) {
                cameraFileUrl = CameraHelper.takePhoto(this, RC_CAMERA)
            }
        }

        homeViewModel.suggestedPhrases.observe(this, Observer {
            it?.forEach {
                binding.suggestedPhrases.addView(Chip(this).apply {
                    text = it
                    setOnClickListener {
                        binding.suggestedPhrases.removeAllViews()
                        loadDefinition((it as Chip).text.toString())
                    }
                })
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CAMERA && resultCode == Activity.RESULT_OK) {
            recogniseText()
        }
    }

    private fun recogniseText() {
        if (cameraFileUrl == null) {
            return
        }

        val fileUri = CameraHelper.getUriFromFilePath(this, cameraFileUrl!!)
        val image = FirebaseVisionImage.fromFilePath(this, fileUri)
        val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer

        detector.processImage(image)
                .addOnSuccessListener {
                    it.textBlocks
                            .flatMap { it.lines }
                            .flatMap { it.text.split(' ') }.forEach {
                                detectedPhrases.removeAllViews()
                                detectedPhrases.addView(Chip(this).apply {
                                    text = it
                                    setOnClickListener {
                                        loadDefinition((it as Chip).text.toString())
                                    }
                                })
                            }

                }
                .addOnFailureListener {
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
                }
    }


    private fun onClose() {
        finish()
    }

    private fun handleOpenSettings() {
        SettingsActivity.start(this)
    }

    private fun handleShareDefinition() {
        // TODO
    }

    private fun handleCopyDefinition(definition: String?) {
        if (definition.isNullOrEmpty()) {
            return
        }

        val clipboardManager = getSystemService(ClipboardManager::class.java)
        clipboardManager.setPrimaryClip(ClipData.newPlainText(homeViewModel.phrase.toString(), definition))
        Toast.makeText(this, R.string.definition_copied, Toast.LENGTH_SHORT).show()
    }

    private fun loadDefinition(phrase: String?) {
        if (!isNetConnected()) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
            return
        }

        homeViewModel
                .loadDefinition(phrase)
                .subscribe({
                    homeViewModel.handleApiSuccess(it)
                }, {
                    LogUtils.log("Failed to get definition!")
                })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtils.checkStoragePermissionGranted(requestCode, grantResults)) {
            cameraFileUrl = CameraHelper.takePhoto(this, RC_CAMERA)
        }
    }
}
