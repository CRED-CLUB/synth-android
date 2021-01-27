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
import android.os.Build
import androidx.annotation.RequiresApi
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.internals.NeuPlatformHelper

@RequiresApi(Build.VERSION_CODES.O)
class ElevatedDrawable(
    neuPlatformAppearance: NeuPlatformAppearance,
    cornerRadius: Float,
    shadowOffset: Float,
) : Drawable() {

    private var backgroundPlatformDrawable = NeuPlatformHelper(
        type = SynthUtils.TYPE_ELEVATED_FLAT,
        neuPlatformAppearance = neuPlatformAppearance,
        cornerRadius = cornerRadius,
        shadowOffset = shadowOffset
    )

    override fun draw(canvas: Canvas) {
        val height = bounds.height()
        val width = bounds.width()
        backgroundPlatformDrawable.draw(canvas, height, width)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
