package com.popalay.shaketoreport

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.jraska.falcon.Falcon
import kotlin.properties.Delegates

internal class ReportDialogFragment : DialogFragment() {

    private var inputDescription: TextInputEditText by Delegates.notNull()
    private var textDeviceInfo: TextView by Delegates.notNull()
    private var buttonCancel: Button by Delegates.notNull()
    private var buttonSend: Button by Delegates.notNull()
    private var imageScreenshot: ImageView by Delegates.notNull()

    companion object {

        private const val ARG_SCREENSHOT_PATH = "ARG_SCREENSHOT_PATH"
        private const val REQUEST_PERMISSIONS = 123

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.report_fragment, container, false)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initView()
            } else {
                Toast.makeText(requireContext(), "Can not proceed operation!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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
        val deviceInfo =
            """Device: ${Build.BRAND.capitalize()} ${Build.MODEL} ${dm.heightPixels}x${dm.widthPixels}
                |Android: ${Build.VERSION.RELEASE}
                |SDK: ${Build.VERSION.SDK_INT}""".trimMargin()
        textDeviceInfo.text = deviceInfo

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS
            )
        } else initView()
    }
}