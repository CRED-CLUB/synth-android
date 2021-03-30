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
import android.graphics.Path
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.withTranslation
import club.cred.synth.SynthUtils.TYPE_ELEVATED_FLAT
import club.cred.synth.SynthUtils.TYPE_ELEVATED_SOFT
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.dp
import club.cred.synth.helper.addParallelogram
import club.cred.synth.helper.withClipOut
import kotlin.math.pow
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.O)
internal class NeuPlatformHelper(
    var type: Int,
    val neuPlatformAppearance: NeuPlatformAppearance,
    cornerRadius: Float = DEFAULT_CORNER_RADIUS.dp,
    val shadowOffset: Float = DEFAULT_SHADOW_OFFSET.dp,
    val spread: Float = DEFAULT_SPREAD
) {

    private val borderOffset: Float = if (type == TYPE_ELEVATED_SOFT) 1f.dp else 0f
    private var pressed: Boolean = false
    private var finalCornerRadius = cornerRadius + spread
    private var clipOffset = (finalCornerRadius - sqrt(finalCornerRadius.pow(2) / 2) + borderOffset).toInt()

    private val lightShadow: BlurHelper = BlurHelper(
        blur = shadowOffset,
        radius = finalCornerRadius,
        shadowColor = neuPlatformAppearance.lightShadowColor
    )
    private val darkShadow: BlurHelper = BlurHelper(
        blur = shadowOffset,
        radius = finalCornerRadius,
        shadowColor = neuPlatformAppearance.darkShadowColor
    )
    private val fillGradient: BlurGradientHelper = BlurGradientHelper(
        blur = 2f.dp,
        radius = finalCornerRadius,
        blurStyle = if (type == TYPE_ELEVATED_FLAT) BlurMaskFilter.Blur.SOLID else BlurMaskFilter.Blur.NORMAL
    )
    private val borderGradient: BlurGradientHelper? =
        if (type == TYPE_ELEVATED_SOFT) {
            BlurGradientHelper(
                blur = 2f.dp,
                radius = finalCornerRadius,
                onlyBorder = true
            )
        } else {
            null
        }

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            lightShadow.radius = field + spread
            darkShadow.radius = field + spread
        }

    init {
        borderGradient?.strokeWidth = 1f.dp
        this.cornerRadius = cornerRadius

        fillGradient.positionArray = floatArrayOf(0f, 1f)
        borderGradient?.positionArray = floatArrayOf(0f, 1f)

        // For flat button, pressed will be false and borderGradient will be null
        updatePressed(pressed, initBypass = true)
    }

    fun updatePressed(pressed: Boolean, initBypass: Boolean = false): Boolean {
        if (!initBypass && this.pressed == pressed) return false
        this.pressed = pressed

        if (pressed) {
            fillGradient.colorArray = intArrayOf(
                neuPlatformAppearance.fillPressedStartColor,
                neuPlatformAppearance.fillPressedEndColor
            )
        } else {
            fillGradient.colorArray = intArrayOf(
                neuPlatformAppearance.fillStartColor,
                neuPlatformAppearance.fillEndColor
            )
        }
        if (pressed) {
            borderGradient?.colorArray = intArrayOf(
                neuPlatformAppearance.borderPressedStartColor,
                neuPlatformAppearance.borderPressedEndColor
            )
        } else {
            borderGradient?.colorArray = intArrayOf(
                neuPlatformAppearance.borderStartColor,
                neuPlatformAppearance.borderEndColor
            )
        }

        return true
    }

    val path = Path()

    fun draw(canvas: Canvas, height: Int, width: Int) {
        val finalHeight = height + spread.toInt() * 2
        val finalWidth = width + spread.toInt() * 2
        val quickReject = canvas.quickReject(0f, 0f, finalWidth.toFloat(), finalHeight.toFloat(), Canvas.EdgeType.AA)
        if (!quickReject) {
            canvas.withTranslation(-spread, -spread) {
                path.reset()
                path.addParallelogram(
                    height = height,
                    width = width,
                    angle = 10.0,
                    cornerRad = DEFAULT_PATH_RAD.dp
                )

                // bg
                withClipOut(path) {
                    withTranslation(shadowOffset / 2, shadowOffset / 2) {
                        darkShadow.draw(canvas, finalHeight, finalWidth)
                    }
                    withTranslation(-shadowOffset / 2, -shadowOffset / 2) {
                        lightShadow.draw(canvas, finalHeight, finalWidth)
                    }
                }

                // fg
                fillGradient.draw(canvas, finalHeight, finalWidth)
                borderGradient?.draw(canvas, finalHeight, finalWidth)
            }
        }
    }

    companion object {
        private const val DEFAULT_CORNER_RADIUS = 20
        private const val DEFAULT_SHADOW_OFFSET = 12
        private const val DEFAULT_SPREAD = 0f
        val DEFAULT_PATH_RAD = 20f
    }
}
