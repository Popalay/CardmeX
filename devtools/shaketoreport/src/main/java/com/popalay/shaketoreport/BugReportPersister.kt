package com.popalay.shaketoreport

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

object BugReportPersister {

    fun save(bugReport: BugReport, screenshot: Bitmap, onSuccess: () -> Unit) {
        val screenshotRef = FirebaseStorage.getInstance().reference.child(bugReport.screenshot)
        val baos = ByteArrayOutputStream()
        screenshot.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val uploadTask = screenshotRef.putBytes(baos.toByteArray())
        uploadTask
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnSuccessListener { _ ->
                FirebaseFirestore.getInstance().collection("bugs").add(bugReport)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { it.printStackTrace() }
            }
    }
}