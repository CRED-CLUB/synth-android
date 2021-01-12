/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import club.cred.synth.R
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.appearances.NeuPlatformAppearance.Companion.getNeuPlatformAppearance
import club.cred.synth.dp
import club.cred.synth.drawables.ElevatedDrawable
import club.cred.synth.drawables.NeuCompatDrawable
import club.cred.synth.dynamicAttr
import club.cred.synth.withAttrs

class ElevatedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var cornerRadius: Float by dynamicAttr(16f.dp, this::updateBackground)
    var platformColor: Int by dynamicAttr(SynthUtils.defaultBaseColor) {
        neuPlatformAppearance = NeuPlatformAppearance(it)
    }
    var shadowOffset: Float by dynamicAttr(DEFAULT_SHADOW_OFFSET.dp, this::updateBackground)
    var neuPlatformAppearance by dynamicAttr(
        NeuPlatformAppearance(SynthUtils.defaultBaseColor), this::updateBackground
    )

    init {
        withAttrs(attrs, R.styleable.ElevatedView) {
            platformColor = getInt(R.styleable.ElevatedView_neuPlatformColor, platformColor)
            cornerRadius = getDimension(R.styleable.ElevatedView_neuCornerRadius, cornerRadius)
            shadowOffset = getDimension(R.styleable.ElevatedView_neuShadowOffset, shadowOffset)
            neuPlatformAppearance = getNeuPlatformAppearance(context, platformColor)
        }

        updateBackground()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun <T> updateBackground(t: T) {
        updateBackground()
    }

    private fun updateBackground() {
        background = if (!SynthUtils.isCompatDevice()) {
            ElevatedDrawable(
                neuPlatformAppearance = neuPlatformAppearance,
                cornerRadius = cornerRadius,
                shadowOffset = shadowOffset
            )
        } else {
            NeuCompatDrawable(
                buttonColor = platformColor,
                cornerRadius = cornerRadius
            )
        }
    }

    companion object {
        const val DEFAULT_SHADOW_OFFSET = 12f
    }
}
