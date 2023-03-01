package com.soywiz.korma.geom

import com.soywiz.kds.pack.*
import com.soywiz.korma.annotations.*

//@KormaValueApi
inline class Scale internal constructor(internal val raw: Float2Pack) {
    val scaleX: Float get() = raw.x
    val scaleY: Float get() = raw.y
    val scaleAvg: Float get() = (scaleX * 0.5f) + (scaleY * 0.5f)

    constructor() : this(1f, 1f)
    constructor(scale: Float) : this(Float2Pack(scale, scale))
    constructor(scale: Double) : this(scale.toFloat())
    constructor(scaleX: Float, scaleY: Float) : this(Float2Pack(scaleX, scaleY))
    constructor(scaleX: Double, scaleY: Double) : this(scaleX.toFloat(), scaleY.toFloat())
}

@KormaMutableApi
sealed interface IScale {
    val scaleX: Double
    val scaleY: Double
}

@KormaMutableApi
data class MScale(
    override var scaleX: Double,
    override var scaleY: Double,
) : IScale

