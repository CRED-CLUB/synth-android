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
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import club.cred.synth.dp

class LinearBlurGradientHelper(
    blur: Float,
    var radius: Float,
    onlyBorder: Boolean = false,
    blurStyle: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL
) {

    var positionArray: FloatArray? = null
    var colorArray: IntArray? = null
    private var paint = Paint()
    private var rectArea = RectF()
    private var paintHeight: Int = 0
    private var paintWidth: Int = 0
    var strokeWidth: Float = 2f.dp
        set(value) {
            field = value
            paint.strokeWidth = field
        }

    init {
        paint.isDither = true
        paint.style = if (onlyBorder) Paint.Style.STROKE else Paint.Style.FILL
        if (blur > 0f) {
            paint.maskFilter = BlurMaskFilter(blur, blurStyle)
        }
    }

    private fun updatePaint(height: Int, width: Int) {
        if (height != this.paintHeight || width != this.paintWidth) {
            paintHeight = height
            paintWidth = width

            if (colorArray != null && positionArray != null) {
                paint.shader = LinearGradient(
                    0f,
                    0f,
                    width.toFloat(),
                    0f,
                    colorArray!!,
                    positionArray,
                    Shader.TileMode.CLAMP
                )
            }
        }
    }

    fun draw(canvas: Canvas, height: Int, width: Int) {
        updatePaint(height, width)
        rectArea.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rectArea, radius, radius, paint)
    }
}
