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
import android.graphics.Paint
import android.graphics.RectF
import club.cred.synth.dp

open class BlurHelper(
    blur: Float,
    radius: Float,
    shadowColor: Int,
    val onlyBorder: Boolean = false
) {

    var blur: Float = 0f.dp
        set(value) {
            if (field != value) {
                field = value
                updateShader()
            }
        }
    var radius: Float = 16f.dp
    var shadowColor: Int = 0x88000000.toInt()
        set(value) {
            if (field != value) {
                field = value
                paint.color = shadowColor
            }
        }
    var strokeWidth: Float = 2f.dp
        set(value) {
            field = value
            paint.strokeWidth = field
        }

    private var paint = Paint()
    private val rectArea = RectF()

    init {
        paint.isDither = true
        paint.style = if (onlyBorder) Paint.Style.STROKE else Paint.Style.FILL
        this.radius = radius
        this.blur = blur
        if (blur == 0f) paint.isAntiAlias = true
        this.shadowColor = shadowColor
    }

    private fun updateShader() {
        if (blur > 0f) {
            paint.maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)
        }
    }

    fun draw(canvas: Canvas, height: Int, width: Int) {
        val quickReject = if (!onlyBorder) {
            canvas.quickReject(-blur, -blur, width + blur, height + blur, Canvas.EdgeType.AA)
        } else {
            canvas.quickReject(0f, 0f, width.toFloat(), height.toFloat(), Canvas.EdgeType.AA)
        }
        if (!quickReject) {
            rectArea.set(0f, 0f, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(rectArea, radius, radius, paint)
        }
    }
}
