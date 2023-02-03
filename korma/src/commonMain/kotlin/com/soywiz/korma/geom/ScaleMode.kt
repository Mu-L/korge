package com.soywiz.korma.geom

import kotlin.math.*

class ScaleMode(
    val name: String? = null,
    val transform: (c: Int, iw: Double, ih: Double, cw: Double, ch: Double) -> Double
) {
    override fun toString(): String = "ScaleMode($name)"

    fun transformW(iw: Double, ih: Double, cw: Double, ch: Double) = transform(0, iw, ih, cw, ch)
    fun transformH(iw: Double, ih: Double, cw: Double, ch: Double) = transform(1, iw, ih, cw, ch)
    fun transform(iw: Double, ih: Double, cw: Double, ch: Double): Size = Size(
        transformW(iw, ih, cw, ch),
        transformH(iw, ih, cw, ch)
    )

    fun transformW(item: Size, container: Size) = transformW(item.width, item.height, container.width, container.height)
    fun transformH(item: Size, container: Size) = transformH(item.width, item.height, container.width, container.height)

    operator fun invoke(item: Size, container: Size): Size =
        transform(item.width, item.height, container.width, container.height)

    operator fun invoke(item: SizeInt, container: SizeInt): SizeInt = SizeInt(
        transformW(item.width.toDouble(), item.height.toDouble(), container.width.toDouble(), container.height.toDouble()).toInt(),
        transformH(item.width.toDouble(), item.height.toDouble(), container.width.toDouble(), container.height.toDouble()).toInt()
    )

    object Provider {
        @Suppress("unused") val LIST = listOf(COVER, SHOW_ALL, EXACT, NO_SCALE)
    }

    companion object {
        val COVER = ScaleMode("COVER") { c, iw, ih, cw, ch ->
            val s0 = cw / iw
            val s1 = ch / ih
            val s = max(s0, s1)
            if (c == 0) iw * s else ih * s
        }

        val SHOW_ALL = ScaleMode("SHOW_ALL") { c, iw, ih, cw, ch ->
            val s0 = cw / iw
            val s1 = ch / ih
            val s = min(s0, s1)
            if (c == 0) iw * s else ih * s
        }

        val FIT get() = SHOW_ALL

        val FILL get() = EXACT

        val EXACT = ScaleMode("EXACT") { c, iw, ih, cw, ch ->
            if (c == 0) cw else ch
        }

        val NO_SCALE = ScaleMode("NO_SCALE") { c, iw, ih, cw, ch ->
            if (c == 0) iw else ih
        }
    }
}

fun Rectangle.applyScaleMode(
    container: Rectangle, mode: ScaleMode, anchor: Anchor, out: Rectangle = Rectangle()
): Rectangle = this.size.applyScaleMode(container, mode, anchor, out)

fun Size.applyScaleMode(container: Rectangle, mode: ScaleMode, anchor: Anchor, out: Rectangle = Rectangle()): Rectangle {
    val outSize = this.applyScaleMode(container.size, mode)
    out.setToAnchoredRectangle(Rectangle(0.0, 0.0, outSize.width, outSize.height), anchor, container)
    return out
}

fun SizeInt.applyScaleMode(container: RectangleInt, mode: ScaleMode, anchor: Anchor, out: RectangleInt = RectangleInt()): RectangleInt =
    this.asDouble().applyScaleMode(container.asDouble(), mode, anchor, out.asDouble()).asInt()

fun SizeInt.applyScaleMode(container: SizeInt, mode: ScaleMode): SizeInt =
    mode(this, container)
fun Size.applyScaleMode(container: Size, mode: ScaleMode): Size =
    mode(this, container)

fun SizeInt.fitTo(container: SizeInt): SizeInt =
    applyScaleMode(container, ScaleMode.SHOW_ALL)
fun Size.fitTo(container: Size): Size =
    applyScaleMode(container, ScaleMode.SHOW_ALL)
