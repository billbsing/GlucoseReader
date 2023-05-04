package com.connectedlife.glucosereader

import android.util.Log

open class GlucoseSectionBase {

    protected fun valdiateCRC(data: SensorByteArray, label: String): Boolean {
        val crc = data.copyFrom(2).crc16
        val headerCRC = data.word(0)
        if (headerCRC == crc) {
            Log.d(TAG, "$label CRC match")
            return true
        }
        else {
            Log.d(TAG, "$label crc mismatch: " + "%04X".format(crc) + " %04X".format(headerCRC))
        }
        return false
    }
}