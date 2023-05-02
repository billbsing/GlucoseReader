package com.connectedlife.glucosereader

import android.R.attr.tag
import android.content.Context
import android.nfc.Tag
import android.nfc.tech.NfcV
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters


class GlucoseReaderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        /*
        val data: Data.Builder = inputData
        val tag: Tag =
        val nfcvTag = NfcV.get(tag)
         */
        return Result.success()
    }
}