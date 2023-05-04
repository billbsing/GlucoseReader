package com.connectedlife.glucosereader

class GlucoseSectionHeader: GlucoseSectionBase() {
    private var calibrationFactor: Int = 0
    private var framState: Int = 0
    private var warmupFlag: Int = 0
    private var endState1: Int = 0
    private var maxLifeTime: Int = 0
    private var endState2: Int = 0

    fun load(data: SensorByteArray) {
        valdiateCRC(data, "Header")
        calibrationFactor = data.word(2)
        framState = data.byte(4)
        warmupFlag = data.byte(5)
        endState1 = data.byte(6)
        maxLifeTime = data.word(7)
        endState2 = data.byte(9)
    }

    override fun toString(): String {
        return "FRAMState: $framState State1: $endState1 State2: $endState2 maxLifeTime: $maxLifeTime"
    }
}