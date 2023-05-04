package com.connectedlife.glucosereader

import android.util.Log

class GlucoseSectionBody: GlucoseSectionBase() {

    companion object {
        const val TREND_TABLE_INDEX_OFFSET = 2
        const val HISTORY_TABLE_INDEX_OFFSET = 3
        const val TREND_TABLE_OFFSET = 4
        const val TABLE_ROW_LENGTH = 6
        const val TREND_TABLE_COUNT = 16
        const val HISTORY_TABLE_OFFSET = TREND_TABLE_OFFSET + (TABLE_ROW_LENGTH * TREND_TABLE_COUNT)
        const val HISTORY_TABLE_COUNT = 32
        const val TIMESTAMP_OFFSET = HISTORY_TABLE_OFFSET + ( TABLE_ROW_LENGTH * HISTORY_TABLE_COUNT)
    }

    private var trendTableIndex: Int = 0
    private var historyTableIndex: Int = 0
    private var timestamp: Int = 0
    private var trendingRows: MutableList<GlucoseItem> = mutableListOf()
    private var historyRows: MutableList<GlucoseItem> = mutableListOf()


    fun load(data: SensorByteArray) {
        valdiateCRC(data, "Body")
        trendTableIndex = data.byte(TREND_TABLE_INDEX_OFFSET)
        historyTableIndex = data.byte(HISTORY_TABLE_INDEX_OFFSET)
        timestamp = data.word(TIMESTAMP_OFFSET)
        trendingRows.clear()
        for (i: Int in 0 until TREND_TABLE_COUNT) {
            var item = GlucoseItem(data.copyFrom(trendRowPosition(i), TABLE_ROW_LENGTH))
            if (item.isValid) {
                trendingRows.add(item)
                Log.d(TAG, "Trend $i: ${item}}")
            }
        }
        historyRows.clear()
        for (i: Int in 0 until HISTORY_TABLE_COUNT) {
            var item = GlucoseItem(data.copyFrom(historyRowPosition(i), TABLE_ROW_LENGTH))
            if (item.isValid) {
                historyRows.add(item)
                Log.d(TAG, "History $i: ${item}}")
            }
        }
    }

    private fun trendRowPosition(index: Int): Int {
        return TREND_TABLE_OFFSET + (index * TABLE_ROW_LENGTH)
    }
    private fun historyRowPosition(index: Int): Int {
        return HISTORY_TABLE_OFFSET + (index * TABLE_ROW_LENGTH)
    }
    override fun toString(): String {
        return "timestamp: $timestamp trendIndex: $trendTableIndex historyIndex: $historyTableIndex"
    }
}