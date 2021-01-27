/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.drawables

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.graphics.withTranslation
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.DividerAppearance
import club.cred.synth.dp
import club.cred.synth.internals.BlurHelper
import club.cred.synth.internals.LinearBlurGradientHelper

class DividerDrawable(
    val drawShadows: Boolean,
    dividerAppearance: DividerAppearance
) : Drawable() {

    private val divider = LinearBlurGradientHelper(0f, 0f)
    private val shadow = BlurHelper(blur = 5f.dp, radius = 0f, shadowColor = 0x60000000)

    init {
        divider.colorArray = dividerAppearance.gradientColors
        divider.positionArray = dividerAppearance.gradientPositions
    }

    override fun draw(canvas: Canvas) {
        divider.draw(canvas, bounds.height(), bounds.width())
        if (!SynthUtils.isCompatDevice() && drawShadows) {
            canvas.withTranslation(0f, 2f.dp) {
                shadow.draw(canvas, bounds.height(), bounds.width())
            }
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
