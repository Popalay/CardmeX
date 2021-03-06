package com.popalay.cardme.data.repository

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.popalay.cardme.api.data.repository.DeviceRepository
import io.reactivex.Completable

internal class DeviceRepository(
    private val context: Context
) : BaseRepository(), DeviceRepository {

    override fun saveToClipboard(label: String, text: String): Completable = Completable.fromAction {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.primaryClip = clip
    }
}