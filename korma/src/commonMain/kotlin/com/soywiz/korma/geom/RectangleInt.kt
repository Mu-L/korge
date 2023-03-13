package com.soywiz.korma.geom

import com.soywiz.korma.annotations.*

@KormaValueApi
data class RectangleInt(
    val position: PointInt,
    val size: SizeInt
) {
    val x: Int get() = position.x
    val y: Int get() = position.y
    val width: Int get() = size.width
    val height: Int get() = size.height

    val area: Int get() = width * height
    val isEmpty: Boolean get() = width == 0 && height == 0
    val isNotEmpty: Boolean get() = !isEmpty
    val mutable: MRectangleInt get() = MRectangleInt(x, y, width, height)

    val left: Int get() = x
    val top: Int get() = y
    val right: Int get() = x + width
    val bottom: Int get() = y + height

    val topLeft: PointInt get() = PointInt(left, top)
    val topRight: PointInt get() = PointInt(right, top)
    val bottomLeft: PointInt get() = PointInt(left, bottom)
    val bottomRight: PointInt get() = PointInt(right, bottom)

    val centerX: Int get() = ((right + left) * 0.5f).toInt()
    val centerY: Int get() = ((bottom + top) * 0.5f).toInt()
    val center: PointInt get() = PointInt(centerX, centerY)

    fun toFloat(): Rectangle = Rectangle(position.toFloat(), size.toFloat())

    operator fun times(scale: Float): RectangleInt = RectangleInt(
        (x * scale).toInt(), (y * scale).toInt(),
        (width * scale).toInt(), (height * scale).toInt()
    )

    operator fun times(scale: Double): RectangleInt = this * scale.toFloat()
    operator fun times(scale: Int): RectangleInt = this * scale.toFloat()

    operator fun div(scale: Float): RectangleInt = RectangleInt(
        (x / scale).toInt(), (y / scale).toInt(),
        (width / scale).toInt(), (height / scale).toInt()
    )

    operator fun div(scale: Double): RectangleInt = this / scale.toFloat()
    operator fun div(scale: Int): RectangleInt = this / scale.toFloat()

    operator fun contains(that: Point): Boolean = contains(that.x, that.y)
    operator fun contains(that: PointInt): Boolean = contains(that.x, that.y)
    fun contains(x: Float, y: Float): Boolean = (x >= left && x < right) && (y >= top && y < bottom)
    fun contains(x: Double, y: Double): Boolean = contains(x.toFloat(), y.toFloat())
    fun contains(x: Int, y: Int): Boolean = contains(x.toFloat(), y.toFloat())

    constructor() : this(PointInt(), SizeInt())
    constructor(x: Int, y: Int, width: Int, height: Int) : this(PointInt(x, y), SizeInt(width, height))

    fun sliceWithBounds(left: Int, top: Int, right: Int, bottom: Int, clamped: Boolean = true): RectangleInt {
        val left = if (!clamped) left else left.coerceIn(0, this.width)
        val right = if (!clamped) right else right.coerceIn(0, this.width)
        val top = if (!clamped) top else top.coerceIn(0, this.height)
        val bottom = if (!clamped) bottom else bottom.coerceIn(0, this.height)
        return fromBounds(this.x + left, this.y + top, this.x + right, this.y + bottom)
    }

    fun sliceWithSize(x: Int, y: Int, width: Int, height: Int, clamped: Boolean = true): RectangleInt =
        sliceWithBounds(x, y, x + width, y + height, clamped)

    fun expanded(border: MarginInt): RectangleInt =
        fromBounds(left - border.left, top - border.top, right + border.right, bottom + border.bottom)

    override fun toString(): String = "Rectangle(x=${x}, y=${y}, width=${width}, height=${height})"

    companion object {
        fun union(a: RectangleInt, b: RectangleInt): RectangleInt = fromBounds(
            kotlin.math.min(a.left, b.left),
            kotlin.math.min(a.top, b.top),
            kotlin.math.max(a.right, b.right),
            kotlin.math.max(a.bottom, b.bottom)
        )

        fun fromBounds(topLeft: PointInt, bottomRight: PointInt): RectangleInt = RectangleInt(topLeft, (bottomRight - topLeft).toSize())
        fun fromBounds(left: Int, top: Int, right: Int, bottom: Int): RectangleInt = fromBounds(PointInt(left, top), PointInt(right, bottom))
    }
}
