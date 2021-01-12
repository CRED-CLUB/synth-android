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
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.withTranslation
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.NeuButtonIconAppearance
import club.cred.synth.dp
import club.cred.synth.internals.BlurGradientHelper

class ButtonIconContainerDrawable(
    val appearance: NeuButtonIconAppearance,
    val iconPitRadius: Float,
    val type: Int,
    drawable: Drawable
) : Drawable() {

    private val blurGradientHelper = BlurGradientHelper(
        blur = if (SynthUtils.isCompatDevice()) 0f else 1.5f.dp,
        radius = iconPitRadius
    )
    private var pressed: Boolean = false

    var buttonIconDrawable: Drawable = drawable
        set(value) {
            field = value
            value.setBounds(0, 0, bounds.width() / 3, bounds.height() / 3)
        }

    init {
        updatePressed(pressed, initBypass = true)
        blurGradientHelper.positionArray = floatArrayOf(0f, 1f)
        bounds = Rect(0, 0, iconPitRadius.toInt() * 2, iconPitRadius.toInt() * 2)
    }

    fun updatePressed(pressed: Boolean, initBypass: Boolean = false): Boolean {
        if (!initBypass && this.pressed == pressed) return false
        this.pressed = pressed

        blurGradientHelper.colorArray = when {
            SynthUtils.isCompatDevice() -> intArrayOf(appearance.compatColor, appearance.compatColor)
            pressed -> intArrayOf(appearance.fillPressedStartColor, appearance.fillPressedEndColor)
            else -> intArrayOf(appearance.fillStartColor, appearance.fillEndColor)
        }

        return true
    }

    override fun draw(canvas: Canvas) {
        blurGradientHelper.draw(canvas, bounds.height(), bounds.width())
        canvas.withTranslation(iconPitRadius * 2 / 3f, iconPitRadius * 2 / 3f) {
            buttonIconDrawable.draw(canvas)
        }
    }

    public override fun onStateChange(stateSet: IntArray?): Boolean {
        val pressed = stateSet?.contains(-android.R.attr.state_enabled) == true ||
            stateSet?.contains(android.R.attr.state_enabled) == false ||
            stateSet?.contains(android.R.attr.state_pressed) == true

        return updatePressed(pressed)
    }

    override fun isStateful() = true

    override fun setState(stateSet: IntArray): Boolean {
        buttonIconDrawable.state = stateSet
        return super.setState(stateSet)
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        buttonIconDrawable.setBounds(0, 0, bounds.width() / 3, bounds.height() / 3)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
