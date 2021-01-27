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

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import club.cred.synth.R
import club.cred.synth.SynthUtils
import club.cred.synth.SynthUtils.TYPE_ELEVATED_FLAT
import club.cred.synth.SynthUtils.TYPE_ELEVATED_SOFT
import club.cred.synth.appearances.NeuButtonIconAppearance
import club.cred.synth.appearances.NeuButtonIconAppearance.Companion.getNeuButtonIconAppearance
import club.cred.synth.appearances.NeuFlatButtonAppearance
import club.cred.synth.appearances.NeuFlatButtonAppearance.Companion.getNeuFlatButtonAppearance
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.appearances.NeuPlatformAppearance.Companion.getNeuPlatformAppearance
import club.cred.synth.dp
import club.cred.synth.drawables.NeuCompatDrawable
import club.cred.synth.drawables.NeuFlatButtonDrawable
import club.cred.synth.drawables.NeuSoftButtonDrawable
import club.cred.synth.dynamicAttr
import club.cred.synth.getDrawableSafe
import club.cred.synth.lighten
import club.cred.synth.views.SynthButton
import club.cred.synth.withAttrs
import kotlin.properties.ReadWriteProperty

internal class SynthButtonDelegate(private val view: View) {

    private val context = view.context

    // Common Properties
    var buttonType: Int = TYPE_ELEVATED_SOFT
    var platformColor: Int by dynamicAttr(SynthUtils.defaultBaseColor) {
        neuPlatformAppearance = NeuPlatformAppearance(it)
    }
    var flatButtonColor: Int by dynamicAttr(SynthUtils.defaultBaseColor) {
        neuFlatButtonAppearance = NeuFlatButtonAppearance(it)
    }
    var compatColor: Int by dynamicAttr(DEFAULT_FLAT_COMPAT_COLOR)
    var cornerRadius: Float by dynamicAttr(DEFAULT_CORNER_RADIUS.dp)
    var neuPlatformAppearance: NeuPlatformAppearance by dynamicAttr(NeuPlatformAppearance(SynthUtils.defaultBaseColor))

    // Only for TYPE_ELEVATED_FLAT
    var neuFlatButtonAppearance: NeuFlatButtonAppearance? by dynamicAttr(null)

    // Properties specific to NeuButton
    var iconPitRadius: Float by dynamicAttr(DEFAULT_ICON_PIT_RADIUS.dp)
    var buttonIconDrawable: Drawable? by dynamicAttr(null)
    var neuButtonIconAppearance: NeuButtonIconAppearance? by dynamicAttr(null)

    fun handleAttrs(attrs: AttributeSet?) {
        view.withAttrs(attrs, R.styleable.NeuButton) {
            buttonType = getInt(R.styleable.NeuButton_neuButtonType, buttonType)
            platformColor = getColor(R.styleable.NeuButton_neuPlatformColor, platformColor)
            flatButtonColor = getColor(R.styleable.NeuButton_neuFlatButtonColor, flatButtonColor)
            val defaultCompatColor =
                if (buttonType == TYPE_ELEVATED_FLAT) {
                    DEFAULT_FLAT_COMPAT_COLOR
                } else {
                    platformColor.lighten(DEFAULT_SOFT_COMPAT_LIGHTEN_FACTOR)
                }
            compatColor = getColor(R.styleable.NeuButton_neuButtonCompatColor, defaultCompatColor)
            cornerRadius = getDimension(R.styleable.NeuButton_neuButtonRadius, cornerRadius)
            neuPlatformAppearance = getNeuPlatformAppearance(context, platformColor)

            if (buttonType == TYPE_ELEVATED_FLAT) {
                neuFlatButtonAppearance = getNeuFlatButtonAppearance(context, flatButtonColor)
            }

            if (view is SynthButton) {
                iconPitRadius = getDimension(R.styleable.NeuButton_neuButtonDrawablePitRadius, cornerRadius - DEFAULT_ICON_RADIUS_DIFF.dp)
                buttonIconDrawable = getDrawableSafe(context, R.styleable.NeuButton_neuButtonDrawable)
                val defaultIconPitColor = if (buttonType == TYPE_ELEVATED_FLAT) flatButtonColor else platformColor
                neuButtonIconAppearance = getNeuButtonIconAppearance(context, buttonType, defaultIconPitColor)
            }
        }

        updateBackground()
    }

    private fun updateBackground() {
        view.background = when {
            !SynthUtils.isCompatDevice() && buttonType == TYPE_ELEVATED_SOFT -> NeuSoftButtonDrawable(
                neuPlatformAppearance = neuPlatformAppearance,
                cornerRadius = cornerRadius
            )
            !SynthUtils.isCompatDevice() && buttonType == TYPE_ELEVATED_FLAT -> NeuFlatButtonDrawable(
                neuPlatformAppearance = neuPlatformAppearance,
                cornerRadius = cornerRadius,
                neuFlatButtonAppearance = neuFlatButtonAppearance!! // won't be null for FLAT
            )
            SynthUtils.isCompatDevice() -> NeuCompatDrawable(
                buttonColor = compatColor,
                cornerRadius = cornerRadius,
                verticalInset = if (buttonType == TYPE_ELEVATED_FLAT) SynthUtils.platformGap else 0f
            )
            else -> error("Illegal state. Please use type elevated_soft or elevated_flat")
        }
    }

    internal fun onTouchEvent(event: MotionEvent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && view.isEnabled) {
            val flag = if (buttonType == TYPE_ELEVATED_FLAT) HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING else 0
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS, flag)
                MotionEvent.ACTION_UP -> view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_RELEASE, flag)
            }
        }
    }

    private inline fun <T> dynamicAttr(
        initialValue: T,
        crossinline onChange: (T) -> Unit = {}
    ): ReadWriteProperty<Any?, T> {
        return view.dynamicAttr(initialValue) {
            onChange(it)
            updateBackground()
        }
    }

    companion object {
        private const val DEFAULT_FLAT_COMPAT_COLOR = 0xFF365BA4.toInt()
        private const val DEFAULT_SOFT_COMPAT_LIGHTEN_FACTOR = 0.03
        private const val DEFAULT_CORNER_RADIUS = 16f
        private const val DEFAULT_ICON_PIT_RADIUS = 8f
        private const val DEFAULT_ICON_RADIUS_DIFF = 8f
    }
}
