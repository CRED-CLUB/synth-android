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
import androidx.core.graphics.withTranslation
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.NeuFlatButtonAppearance
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.helper.withClipOut
import club.cred.synth.internals.NeuFlatButtonHelper
import club.cred.synth.internals.NeuPlatformHelper
import kotlin.math.pow
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.O)
class NeuFlatButtonDrawable(
    neuPlatformAppearance: NeuPlatformAppearance,
    cornerRadius: Float,
    neuFlatButtonAppearance: NeuFlatButtonAppearance
) : Drawable() {

    private val buttonRadius = cornerRadius - SynthUtils.platformGap
    private var clipOffset = (buttonRadius - sqrt(buttonRadius.pow(2) / 2) + SynthUtils.platformGap).toInt()

    private var backgroundPlatformDrawable = NeuPlatformHelper(
        type = SynthUtils.TYPE_ELEVATED_FLAT,
        neuPlatformAppearance = neuPlatformAppearance,
        cornerRadius = cornerRadius,
    )
    private var buttonHelper = NeuFlatButtonHelper(
        cornerRadius = buttonRadius,
        neuFlatButtonAppearance = neuFlatButtonAppearance
    )

    override fun draw(canvas: Canvas) {
        val height = bounds.height()
        val width = bounds.width()
        canvas.withClipOut(clipOffset, clipOffset, width - clipOffset, height - clipOffset) {
            backgroundPlatformDrawable.draw(canvas, height, width)
        }
        canvas.withTranslation(SynthUtils.platformGap, SynthUtils.platformGap) {
            buttonHelper.draw(canvas, (height - SynthUtils.platformGap * 2).toInt(), (width - SynthUtils.platformGap * 2).toInt())
        }
    }

    public override fun onStateChange(stateSet: IntArray?): Boolean {
        // TODO
        val pressed = /*stateSet?.contains(-android.R.attr.state_enabled) == true ||
            stateSet?.contains(android.R.attr.state_enabled) == false ||*/
            stateSet?.contains(android.R.attr.state_pressed) == true

        return buttonHelper.updatePressed(pressed)
    }

    override fun isStateful() = true

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
