package com.connectedlife.glucosereader

import android.util.Log

class SensorByteArray(val data: ByteArray) {

    companion object Factory {
        fun sensorByteArrayOf(data: ByteArray): SensorByteArray {
            return SensorByteArray(data)
        }
    }

    fun log(blockSize: Int = 16) {
        val values: MutableList<String> = mutableListOf()
        var counter: Int = 0
        var index: Int = 0
        for (value: Byte in data) {
            values.add("%02X".format(value))
            counter++
            if (counter == blockSize) {
                Log.d(TAG, "0x%04X ".format(index) + values.joinToString(separator = " "))
                counter = 0;
                index += blockSize;
                values.clear()
            }
        }
        if (values.size > 0) {
            Log.d(TAG, "0x%04X ".format(index) + values.joinToString(separator = " "))
        }
    }

    fun copyFrom(index: Int, length: Int = -1): SensorByteArray {
        var copyLength = data.size
        if (length > 0) {
            copyLength =  index + length
        }
        return SensorByteArray(data.copyOfRange(index, copyLength))
    }

    fun word(index: Int): Int {
        return (data.get(index).toInt() and 0xFF) or (((data.get(index + 1).toInt() and 0xFF)) shl 8 and 0xFFFF )
    }

    fun byte(index: Int): Int {
        return (data.get(index).toInt() and 0xFF)
    }

    private fun bitReverse(data: Byte): Int {
        return data.toInt() shl 7 and 0x80 or (data.toInt() shl 5 and 0x40) or (data.toInt() shl 3 and 0x20) or (data.toInt() shl 1 and 0x10) or (data.toInt() shr 7 and 0x01) or (data.toInt() shr 5 and 0x02) or (data.toInt() shr 3 and 0x04) or (data.toInt() shr 1 and 0x08)
    }

    val crc16: Int get() {
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