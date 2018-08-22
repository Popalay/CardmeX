package com.popalay.shaketoreport

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.jraska.falcon.Falcon
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.tryOrNull
import com.popalay.cardme.core.widget.ProgressMaterialButton
import java.util.Date

internal class ReportDialogFragment : DialogFragment() {

    private val inputDescription: TextInputEditText by bindView(R.id.input_description)
    private val imageScreenshot: ImageView by bindView(R.id.image_screenshot)
    private val textDeviceInfo: TextView by bindView(R.id.text_device_info)
    private val buttonCancel: Button by bindView(R.id.button_cancel)
    private val buttonSend: ProgressMaterialButton by bindView(R.id.button_send)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Material_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.report_fragment, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        initView()
    }

    private fun initView() {
        tryOrNull { Falcon.takeScreenshotBitmap(activity) }?.also { screenshot ->
            imageScreenshot.setImageBitmap(screenshot)
            val dm = resources.displayMetrics
            val deviceInfo = DeviceInfo(
                device = "${Build.BRAND.capitalize()} ${Build.MODEL}",
                display = "${dm.heightPixels}x${dm.widthPixels}",
                androidVersion = Build.VERSION.RELEASE,
                sdkVersion = "${Build.VERSION.SDK_INT}"
            )
            textDeviceInfo.text = deviceInfo.toString()
            inputDescription.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    buttonSend.isEnabled = !s.isNullOrBlank()
                }
            })
            buttonCancel.setOnClickListener { dismiss() }
            buttonSend.isEnabled = !inputDescription.text.isNullOrBlank()
            buttonSend.setOnClickListener { saveBugReport(deviceInfo, screenshot) }
        }
    }

    private fun saveBugReport(deviceInfo: DeviceInfo, screenshot: Bitmap) {
        val bugReport = BugReport(
            description = inputDescription.text?.trim().toString(),
            deviceInfo = deviceInfo,
            screenshot = "/bugs/${Date().time}.jpg",
            createdAt = Timestamp.now()
        )
        buttonSend.isProgress = true
        buttonCancel.isEnabled = false
        BugReportPersister.save(bugReport, screenshot) {
            buttonSend.isProgress = false
            buttonCancel.isEnabled = true
            LastReportTimePersister.save(requireContext(), Date())
            dismiss()
        }
    }
}