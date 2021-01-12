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
import club.cred.synth.SynthUtils.TYPE_ELEVATED_SOFT
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.internals.NeuPlatformHelper

@RequiresApi(Build.VERSION_CODES.O)
class NeuSoftButtonDrawable(
    neuPlatformAppearance: NeuPlatformAppearance,
    cornerRadius: Float
) : Drawable() {

    private var backgroundPlatformDrawable = NeuPlatformHelper(
        type = TYPE_ELEVATED_SOFT,
        neuPlatformAppearance = neuPlatformAppearance,
        cornerRadius = cornerRadius,
    )

    override fun draw(canvas: Canvas) {
        val height = bounds.height()
        val width = bounds.width()
        backgroundPlatformDrawable.draw(canvas, height, width)
    }

    public override fun onStateChange(stateSet: IntArray?): Boolean {
        val pressed = stateSet?.contains(-android.R.attr.state_enabled) == true ||
            stateSet?.contains(android.R.attr.state_enabled) == false ||
            stateSet?.contains(android.R.attr.state_pressed) == true

        return backgroundPlatformDrawable.updatePressed(pressed)
    }

    override fun isStateful() = true

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
