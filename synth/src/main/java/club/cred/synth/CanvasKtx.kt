/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.helper

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Region
import android.graphics.Shader
import android.os.Build
import android.text.TextPaint
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.math.hypot
import kotlin.math.tan

/**
 * clips everything inside the rect.
 */
@RequiresApi(Build.VERSION_CODES.O)
inline fun Canvas.withClipOut(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    clipOutRect(left, top, right, bottom)
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}

/**
 * clips everything inside the path.
 */
@Suppress("DEPRECATION")
inline fun Canvas.withClipOut(
    path: Path,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        clipOutPath(path)
    } else {
        clipPath(path, Region.Op.DIFFERENCE)
    }
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}

fun TextView.setGradientTextColors(vararg colors: Int) {
    setTextColor(colors[0])
    val paint: TextPaint = paint
    val width: Float = paint.measureText(text, 0, text.length)

    val textShader: Shader = LinearGradient(0f, textSize / 2, width, textSize / 2, colors, null, Shader.TileMode.CLAMP)
    paint.shader = textShader
}

fun Path.addParallelogram(
    height: Int,
    width: Int,
    angle: Double,
    cornerRad: Float = 0f
) {

    val movePath = (height * tan(Math.toRadians(angle))).toFloat()

    val topLeft = PointF(movePath, 0f)
    val topRight = PointF(width.toFloat(), 0f)
    val bottomRight = PointF(width.toFloat() - movePath, height.toFloat())
    val bottomLeft = PointF(0f, height.toFloat())

    val points = listOf(topLeft, topRight, bottomRight, bottomLeft, topLeft)

    for (i in 0 until points.size - 1) {
        val lineStart = getLineStart(points[i], points[i + 1], cornerRad)
        val lineEnd = getLineEnd(points[i], points[i + 1], cornerRad)

        if (i == 0) {
            moveTo(lineStart.x, lineStart.y)
        } else {
            quadTo(points[i].x, points[i].y, lineStart.x, lineStart.y)
        }

        lineTo(lineEnd.x, lineEnd.y)
    }

    val topLineStart = getLineStart(topLeft, topRight, cornerRad)
    quadTo(topLeft.x, topLeft.y, topLineStart.x, topLineStart.y)

    close()
}

private fun getDist(p1: PointF, p2: PointF): Float = hypot(p1.x - p2.x, p1.y - p2.y)

private fun getLineStart(p1: PointF, p2: PointF, cornerRad: Float): PointF {
    // above 0.5 means the corner rad is greater than half of the side. This means that two curves will
    // intersect. To prevent that, we cap the ratio at 0.5
    val radLengthRatio: Float = (cornerRad / getDist(p1, p2)).takeIf { it <= 0.5f } ?: 0.5f
    return PointF(
        (1.0f - radLengthRatio) * p1.x + radLengthRatio * p2.x,
        (1.0f - radLengthRatio) * p1.y + radLengthRatio * p2.y
    )
}

private fun getLineEnd(p1: PointF, p2: PointF, cornerRad: Float): PointF {
    val radLengthRatio: Float = (cornerRad / getDist(p1, p2)).takeIf { it <= 0.5f } ?: 0.5f
    return PointF(
        radLengthRatio * p1.x + (1.0f - radLengthRatio) * p2.x,
        radLengthRatio * p1.y + (1.0f - radLengthRatio) * p2.y
    )
}
