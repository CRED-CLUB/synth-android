/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.internals

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import club.cred.synth.dp
import club.cred.synth.helper.addParallelogram
import kotlin.math.pow
import kotlin.math.sqrt

class BlurGradientHelper(
    blur: Float,
    var radius: Float,
    onlyBorder: Boolean = false,
    blurStyle: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL
) {

    var positionArray: FloatArray? = null
    var colorArray: IntArray? = null
        set(value) {
            field = value
            forceUpdatePaint = true
        }
    var strokeWidth: Float = 2f.dp
        set(value) {
            field = value
            paint.strokeWidth = field
        }

    private var paint = Paint()
    private var rectArea = RectF()
    private var paintHeight: Int = 0
    private var paintWidth: Int = 0
    private var forceUpdatePaint: Boolean = false

    init {
        paint.isDither = true
        paint.style = if (onlyBorder) Paint.Style.STROKE else Paint.Style.FILL
        if (blur > 0f) {
            paint.maskFilter = BlurMaskFilter(blur, blurStyle)
        }
    }

    private fun updatePaint(height: Int, width: Int) {
        if (height != this.paintHeight || width != this.paintWidth || forceUpdatePaint) {
            forceUpdatePaint = false
            paintHeight = height
            paintWidth = width

            // ----- little math here -----
            val hypotenuse = sqrt(paintHeight.toDouble().pow(2) + paintWidth.toDouble().pow(2))
            // area of square = height * width = hypotenuse * (maximum distance length perpendicular to hypotenuse)
            val heightOfGradient = paintHeight * paintWidth * 2 / hypotenuse

            val y = width / hypotenuse * (heightOfGradient / 2)
            val x = height / hypotenuse * (heightOfGradient / 2)

            if (colorArray != null && positionArray != null) {
                paint.shader = LinearGradient(
                    width / 2 - x.toFloat(),
                    height / 2 - y.toFloat(),
                    width / 2 + x.toFloat(),
                    height / 2 + y.toFloat(),
                    colorArray!!,
                    positionArray,
                    Shader.TileMode.CLAMP
                )
            }
        }
    }

    val path = Path()
    val pathEffect = CornerPathEffect(NeuPlatformHelper.DEFAULT_PATH_RAD)

    fun draw(canvas: Canvas, height: Int, width: Int) {
        updatePaint(height, width)
        // rectArea.set(0f, 0f, width.toFloat(), height.toFloat())
        // canvas.drawRoundRect(rectArea, radius, radius, paint)

        path.reset()
        path.addParallelogram(height, width, 10.0, cornerRad = NeuPlatformHelper.DEFAULT_PATH_RAD.dp)
        canvas.drawPath(path, paint)
    }
}
