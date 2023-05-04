package com.connectedlife.glucosereader

class GlucoseSectionFooter: GlucoseSectionBase() {

    private var countryCode: Int = 0
    private var maxTimeDays: Int = 0
    private var warmUpThreshold: Int = 0
    private var adcIRQSync: Int = 0
    private var callibrationAverage: Int = 0
    private var measureDataThreshold: Int = 0
    private var bypassChecks: Int = 0
    private var lockTableFlag: Int = 0

    fun load(data: SensorByteArray) {
        valdiateCRC(data, "Footer")
        countryCode = data.word(2)
        maxTimeDays = data.word(6)
        adcIRQSync = data.byte(9)
        warmUpThreshold = data.byte(10)
        callibrationAverage = data.byte(11)
        measureDataThreshold = data.byte(13)
        bypassChecks = data.byte(15)
        lockTableFlag = data.byte(16)
    }
    override fun toString(): String {
        return "maxTime: $maxTimeDays warmupThreshold: $warmUpThreshold"
    }
}