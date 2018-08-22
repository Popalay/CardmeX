package com.popalay.shaketoreport

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
import com.jraska.falcon.Falcon
import java.util.Date
import kotlin.properties.Delegates

internal class ReportDialogFragment : DialogFragment() {

    private var inputDescription: TextInputEditText by Delegates.notNull()
    private var textDeviceInfo: TextView by Delegates.notNull()
    private var buttonCancel: Button by Delegates.notNull()
    private var buttonSend: Button by Delegates.notNull()
    private var imageScreenshot: ImageView by Delegates.notNull()

    companion object {

        private const val ARG_SCREENSHOT_PATH = "ARG_SCREENSHOT_PATH"

        fun newInstance(screenshotPath: String) = ReportDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SCREENSHOT_PATH, screenshotPath)
            }
        }
    }

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
        imageScreenshot = view.findViewById(R.id.image_screenshot)
        inputDescription = view.findViewById(R.id.input_description)
        textDeviceInfo = view.findViewById(R.id.text_device_info)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonSend = view.findViewById(R.id.button_send)
        initView()
    }

    private fun initView() {
        val screenshot = Falcon.takeScreenshotBitmap(activity)
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
        buttonSend.setOnClickListener { saveBugReport(deviceInfo) }
    }

    private fun saveBugReport(deviceInfo: DeviceInfo) {
        //TODO upload screenshot
        val bugReport = BugReport(
            description = inputDescription.text?.trim().toString(),
            deviceInfo = deviceInfo,
            //screenshot = screenShot,
            createdAt = Date()
        )
        //TODO save bug report
    }
}