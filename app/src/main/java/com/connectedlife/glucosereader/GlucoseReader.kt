package com.connectedlife.glucosereader

import android.nfc.Tag
import android.nfc.tech.NfcV
import android.util.Log
import com.connectedlife.glucosereader.SensorByteArray.Factory.sensorByteArrayOf


class GlucoseReader(val tag: Tag) {

    companion object {
        const val TAG: String = "GlucoseReader"
    }
    private val handle: NfcV = NfcV.get(tag)
    val uid: ByteArray? = tag.id

    var sensorType: String = ""
    var uuid: String = ""

    private val LIBRE1_ID = byteArrayOf(0xA2.toByte(), 0x08.toByte(), 0x00.toByte())
    private val LIBRE_OLD_ID = byteArrayOf(0xE9.toByte(), 0x00.toByte(), 0x00.toByte())
    private val LIBRE1_JAPAN_ID = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x04.toByte())


    fun readIdentity() {
        val idCommandList = byteArrayOf(0x02.toByte(), 0xa1.toByte(), 0x07.toByte())
        val rxData: ByteArray = sendCommand(idCommandList)
        val identData: ByteArray = rxData.sliceArray(IntRange(1, 3))
        /*
        for (item: Byte in identData) {
            Log.d(TAG, "value: %x".format(item))
        }
        */
        if (identData contentEquals LIBRE1_ID) {
            sensorType = "Libre 1"
        }
        else if (identData contentEquals LIBRE_OLD_ID) {
            sensorType = "Libre Old"
        }
        else if (identData contentEquals LIBRE1_JAPAN_ID) {
            sensorType = "Libre 1 Japan"
        }
        else {
            sensorType = "Unknown"
        }
        Log.d(TAG, "sensorType $sensorType")
    }

    fun readData(): ByteArray {
        var data = ByteArray(0)
        Log.d(TAG, "reading block data")
        for (blockIndex in 0 until 43 step 3) {
            Log.d(TAG, "reading: $blockIndex")
            val commandList = byteArrayOf(0x02.toByte(), 0x23.toByte(), blockIndex.toByte(), 0x02.toByte())
            val rxData: ByteArray = sendCommand(commandList)
            data += rxData.drop(1)
        }
        Log.d(TAG, "reading completed ${data.size}")
        return data
    }

    fun readUUID() {
        val idCommandList = byteArrayOf(0x26.toByte(), 0x01.toByte(), 0x00.toByte())
        val rxData: ByteArray = sendCommand(idCommandList)
        var data = sensorByteArrayOf(rxData)
        data = data.copyFrom(2)
        Log.d(TAG, "uuid: ")
        data.log()
        uuid = data.toHexString()
        Log.d(TAG, "UUID: ${uuid}")
    }

    private fun sendCommand(commandList: ByteArray): ByteArray {
        if (handle.isConnected) {
            handle.close()
        }
        handle.connect()
        val rxData: ByteArray = handle.transceive(commandList)
        handle.close()
        return rxData
    }
}