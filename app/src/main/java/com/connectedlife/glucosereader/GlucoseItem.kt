package com.connectedlife.glucosereader

class GlucoseItem(val data: SensorByteArray ) {
    private val glucoseRaw: Int = data.word(0)
    private val glucoseMGDL: Float get() = (glucoseRaw.toFloat() / 10) * 100
    private val glucoseMMOL: Float get() = (glucoseRaw.toFloat() / 18) * 10
    private val spare1: Int = data.byte(2)
    private val temperatureRaw: Int = data.word(3) and 0x3FF
    private val temperatureC: Float = temperatureRaw.toFloat() / 10
    private val spare2: Int = data.byte(5)

    val isValid: Boolean get() = glucoseRaw > 0

    override fun toString(): String {
        return "Glucose: " + "%.0f".format(glucoseMGDL) + " mg/dl Glucose: " + "%.2f".format(glucoseMMOL) +
            " mmol/l Temperature: " + "%.1f".format(temperatureC) + " spare1: $spare1 spare2: $spare2"
    }
}