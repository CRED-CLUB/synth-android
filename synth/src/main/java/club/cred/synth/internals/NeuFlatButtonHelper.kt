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

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation
import club.cred.synth.appearances.NeuFlatButtonAppearance
import club.cred.synth.dp

@RequiresApi(Build.VERSION_CODES.O)
internal class NeuFlatButtonHelper(
    cornerRadius: Float = DEFAULT_CORNER_RADIUS.dp,
    val spread: Float = DEFAULT_SPREAD,
    val neuFlatButtonAppearance: NeuFlatButtonAppearance
) {

    private var pressed: Boolean = false
    private var finalCornerRadius = cornerRadius + spread
    private val pressedDepth = DEFAULT_DEPTH.dp

    private val border1: BlurGradientHelper = BlurGradientHelper(
        blur = BORDER1_BLUR.dp,
        radius = finalCornerRadius,
        onlyBorder = true
    )
    private val border2: BlurGradientHelper = BlurGradientHelper(
        blur = 0f,
        radius = finalCornerRadius,
        onlyBorder = true
    )
    private val sharpPaint: BlurGradientHelper = BlurGradientHelper(
        blur = 0f,
        radius = finalCornerRadius
    )
    private val innerShadowHelper: BlurHelper = BlurHelper(
        blur = pressedDepth,
        radius = finalCornerRadius,
        shadowColor = neuFlatButtonAppearance.pressedDarkShadowColor,
        onlyBorder = true
    )
    private val path = Path()
    private val rectF = RectF()
    var cornerRadius: Float = 0f
        set(value) {
            field = value
            border1.radius = field + spread
            border2.radius = field + spread
        }

    init {
        this.cornerRadius = cornerRadius
        val colorArray = intArrayOf(
            neuFlatButtonAppearance.fillStartColor,
            neuFlatButtonAppearance.fillEndColor
        )
        val positionArray = floatArrayOf(0f, 1f)
        sharpPaint.colorArray = colorArray
        sharpPaint.positionArray = positionArray
        border1.positionArray = positionArray
        border2.positionArray = positionArray
        border1.strokeWidth = BORDER1_WIDTH.dp
        border2.strokeWidth = BORDER2_WIDTH.dp
        border1.colorArray = intArrayOf(
            neuFlatButtonAppearance.border1StartColor,
            neuFlatButtonAppearance.border1EndColor
        )
        border2.colorArray = intArrayOf(
            neuFlatButtonAppearance.border2StartColor,
            neuFlatButtonAppearance.border2EndColor
        )
        innerShadowHelper.strokeWidth = pressedDepth
    }

    fun updatePressed(pressed: Boolean): Boolean {
        if (this.pressed == pressed) return false
        this.pressed = pressed
        return true
    }

    fun draw(canvas: Canvas, height: Int, width: Int) {
        val finalHeight = height + spread.toInt() * 2
        val finalWidth = width + spread.toInt() * 2
        val finalHeightF = height.toFloat()
        val finalWidthF = width.toFloat()
        val quickReject = canvas.quickReject(0f, 0f, finalWidthF, finalHeightF, Canvas.EdgeType.AA)
        if (!quickReject) {
            canvas.withTranslation(-spread, -spread) {
                sharpPaint.draw(canvas, finalHeight, finalWidth)
                if (pressed) {
                    if (rectF.width() != finalWidthF || rectF.height() != finalHeightF) {
                        rectF.set(0f, 0f, finalWidthF, finalHeightF)
                        path.reset()
                        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
                    }
                    canvas.withClip(path) {
                        innerShadowHelper.draw(canvas, height + pressedDepth.toInt(), width + pressedDepth.toInt())
                    }
                } else {
                    border1.draw(canvas, finalHeight, finalWidth)
                    border2.draw(canvas, finalHeight, finalWidth)
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 20
        private const val DEFAULT_DEPTH = 10
        private const val DEFAULT_SPREAD = 0f
        private const val BORDER1_BLUR = .2f
        private const val BORDER1_WIDTH = .9f
        private const val BORDER2_WIDTH = .35f
    }
}
