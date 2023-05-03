package com.connectedlife.glucosereader

import android.hardware.Sensor
import android.util.Log

class SensorData() {

    val HEADER_LENGTH: Int = 24
    val BODY_LENGTH: Int = 296
    val FOOTER_LENGTH: Int = 24

    val TAG: String = "SensorData"
    var header: ByteArray = ByteArray(0)
    var body: ByteArray = ByteArray(0)
    var footer: ByteArray = ByteArray(0)

    fun load(data: ByteArray) {
        loadHeader(data)
        loadBody(data)
        loadFooter(data)
    }

    fun loadHeader(data: ByteArray) {
        header = data.copyOfRange(0, HEADER_LENGTH)
        valdiateCRC(header, "Header")
    }
    fun loadBody(data: ByteArray) {
        body = data.copyOfRange(HEADER_LENGTH, HEADER_LENGTH + BODY_LENGTH )
        valdiateCRC(body, "Body")
    }

    fun loadFooter(data: ByteArray) {
        footer = data.copyOfRange(HEADER_LENGTH + BODY_LENGTH, HEADER_LENGTH + BODY_LENGTH + FOOTER_LENGTH )
        valdiateCRC(footer, "Footer")
    }

    fun valdiateCRC(data: ByteArray, label: String): Boolean {
        val crc = crc16(data.copyOfRange(2, data.size))
        val headerCRC = SensorUtils.getWord(data, 0)
        if (headerCRC == crc) {
            Log.d(TAG, "$label CRC match")
            return true
        }
        else {
            Log.d(TAG, "$label crc mismatch: " + "%04X".format(crc) + " %04X".format(headerCRC))
        }
        return false
    }

    fun bitReverse(data: Byte): Int {
        return data.toInt() shl 7 and 0x80 or (data.toInt() shl 5 and 0x40) or (data.toInt() shl 3 and 0x20) or (data.toInt() shl 1 and 0x10) or (data.toInt() shr 7 and 0x01) or (data.toInt() shr 5 and 0x02) or (data.toInt() shr 3 and 0x04) or (data.toInt() shr 1 and 0x08)
    }

    fun crc16(data: ByteArray): Int {
        var crc: Int = 0xffff
        for (value in data) {
            crc = ((crc shr 8) and 0xffff) or ((crc shl 8) and 0xffff)
            crc = crc xor bitReverse(value)
            crc = crc xor (((crc and 0xff) shr 4) and 0xffff)
            crc = crc xor ((crc shl 12) and 0xffff)
            crc = crc xor (((crc and 0xff) shl 5) and 0xffff)
        }
        return crc
    }
}