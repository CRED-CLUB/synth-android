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

import android.graphics.Canvas
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.PitViewAppearance

interface PitHelperInterface {

    fun draw(canvas: Canvas, height: Int, width: Int)

    @Suppress("LongParameterList") // acts as a builder
    companion object {
        fun newInstance(
            pitViewAppearance: PitViewAppearance,
            cornerRadius: Float,
            pressedDepth: Float,
            drawBackground: Boolean,
            drawShadows: Boolean,
            isSoft: Boolean,
            isCard: Boolean
        ): PitHelperInterface =
            if (SynthUtils.isCompatDevice()) {
                PitHelperCompat(
                    pitViewAppearance = pitViewAppearance,
                    cornerRadius = cornerRadius,
                    drawBackground = drawBackground
                )
            } else {
                PitHelper(
                    pitViewAppearance = pitViewAppearance,
                    cornerRadius = cornerRadius,
                    pressedDepth = pressedDepth,
                    drawShadows = drawShadows,
                    isSoft = isSoft,
                    isCard = isCard
                )
            }
    }
}
