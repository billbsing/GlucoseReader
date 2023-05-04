package com.connectedlife.glucosereader

import android.util.Log
import com.connectedlife.glucosereader.SensorByteArray.Factory.sensorByteArrayOf

class GlucoseDecoder(val data: ByteArray) {

    companion object {
        const val TAG: String = "SensorData"
        const val HEADER_LENGTH: Int = 24
        const val BODY_LENGTH: Int = 296
        const val FOOTER_LENGTH: Int = 24
    }

    private val header: GlucoseSectionHeader = GlucoseSectionHeader()
    private var body: GlucoseSectionBody = GlucoseSectionBody()
    private var footer: GlucoseSectionFooter = GlucoseSectionFooter()
    val sensorData: SensorByteArray get() = SensorByteArray(data)

    fun load() {
        val headerData = sensorByteArrayOf(data.copyOfRange(0, HEADER_LENGTH))
        header.load(headerData)
        Log.d(TAG, header.toString())

        val bodyData = sensorByteArrayOf(data.copyOfRange(HEADER_LENGTH, HEADER_LENGTH + BODY_LENGTH))
        body.load(bodyData)
        Log.d(TAG, body.toString())

        val footerData = sensorByteArrayOf(data.copyOfRange(HEADER_LENGTH + BODY_LENGTH, HEADER_LENGTH + BODY_LENGTH + FOOTER_LENGTH))
        footer.load(footerData)
        Log.d(TAG, footer.toString())
    }

}