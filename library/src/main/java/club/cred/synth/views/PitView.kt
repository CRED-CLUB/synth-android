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
import club.cred.synth.appearances.PitViewAppearance
import club.cred.synth.appearances.PitViewAppearance.Companion.getPitViewAppearance
import club.cred.synth.dp
import club.cred.synth.drawables.PitDrawable
import club.cred.synth.dynamicAttr
import club.cred.synth.internals.PitHelper
import club.cred.synth.withAttrs

class PitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var pitColor: Int by dynamicAttr(SynthUtils.defaultBaseColor) {
        pitViewAppearance = PitViewAppearance(pitColor)
    }
    var pitViewAppearance: PitViewAppearance by dynamicAttr(
        PitViewAppearance(SynthUtils.defaultBaseColor), this::updateBackground
    )
    var cornerRadiusArg: Float by dynamicAttr(20f.dp, this::updateBackground)
    var pitClipType: Int by dynamicAttr(PitDrawable.NO_CLIP, this::updateBackground)
    var drawBackground by dynamicAttr(true, this::updateBackground)
    var drawShadows by dynamicAttr(true, this::updateBackground)
    var isSoft by dynamicAttr(true, this::updateBackground)
    var isCard by dynamicAttr(true, this::updateBackground)
    var pressedDepth: Float by dynamicAttr(PitHelper.DEFAULT_PRESSED_DEPTH, this::updateBackground)

    init {
        withAttrs(attrs, R.styleable.PitView) {
            pitColor = getInt(R.styleable.PitView_pitColor, pitColor)
            pitViewAppearance = getPitViewAppearance(context, pitColor)
            pitClipType = getInt(R.styleable.PitView_pitClipType, pitClipType)
            cornerRadiusArg = getDimension(R.styleable.PitView_neuCornerRadius, cornerRadiusArg)
            drawBackground = getBoolean(R.styleable.PitView_drawBackground, drawBackground)
            drawShadows = getBoolean(R.styleable.PitView_drawShadows, drawShadows)
            isSoft = getBoolean(R.styleable.PitView_isSoft, isSoft)
            isCard = getBoolean(R.styleable.PitView_isCard, isCard)
            pressedDepth = getDimension(R.styleable.PitView_pitDepth, pressedDepth)
        }

        updateBackground()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun <T> updateBackground(t: T) {
        updateBackground()
    }

    private fun updateBackground() {
        background = PitDrawable(
            pitViewAppearance = pitViewAppearance,
            cornerRadiusArg = cornerRadiusArg,
            drawBackground = drawBackground,
            drawShadows = drawShadows,
            isSoft = isSoft,
            isCard = isCard,
            pressedDepth = pressedDepth,
            pitClipType = pitClipType
        )
    }
}
