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
import club.cred.synth.internals.BlurHelper

class NeuCompatDrawable(
    val buttonColor: Int,
    cornerRadius: Float,
    val verticalInset: Float = 0f
) : Drawable() {

    private val darkBackground: BlurHelper = BlurHelper(
        blur = 0f,
        radius = cornerRadius,
        shadowColor = buttonColor
    )

    override fun draw(canvas: Canvas) {
        val height = bounds.height() - verticalInset.toInt() * 2
        val width = bounds.width()

        canvas.withTranslation(0f, verticalInset) {
            darkBackground.draw(canvas, height, width)
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
