package com.connectedlife.glucosereader

import android.util.Log

class SensorUtils {
    companion object{
        fun logData(data: ByteArray) {
            val values: MutableList<String> = mutableListOf()
            var counter: Int = 0
            var index: Int = 0
            for (value: Byte in data) {
                values.add("%02X".format(value))
                counter++
                if (counter == 16) {
                    Log.d(TAG, "0x%04X ".format(index) + values.joinToString(separator = " "))
                    counter = 0;
                    index += 16;
                    values.clear()
                }
            }
            if (values.size > 0) {
                Log.d(TAG, "0x%04X ".format(index) + values.joinToString(separator = " "))
            }
        }
        fun getWord(data: ByteArray, index: Int): Int {
            return (data.get(0).toInt() and 0xFF) or (((data.get(1).toInt() and 0xFF)) shl 8 and 0xFFFF )
        }
    }
}