/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.appearances

import android.content.Context
import android.content.res.TypedArray
import club.cred.synth.R
import club.cred.synth.darken
import club.cred.synth.lighten
import club.cred.synth.withAttrs

@Suppress("LongParameterList") // acts as a builder
data class PitViewAppearance(
    val lightInnerShadowColor: Int,
    val darkInnerShadowColor: Int,
    val softEdgeColor: Int,
    val compatFillColor: Int
) {

    constructor(pitColor: Int) : this(
        pitColor.lighten(LIGHT_INNER_SHADOW_FRACTION),
        pitColor.darken(DARK_INNER_SHADOW_FRACTION),
        pitColor,
        pitColor.darken(FILL_COLOR_FRACTION)
    )

    companion object {
        private const val LIGHT_INNER_SHADOW_FRACTION = 0.05
        private const val DARK_INNER_SHADOW_FRACTION = 0.30
        private const val FILL_COLOR_FRACTION = 0.06

        fun TypedArray.getPitViewAppearance(context: Context, pitColor: Int): PitViewAppearance {
            val pitViewAppearance = getResourceId(R.styleable.PitView_pitViewAppearance, -1)
            if (pitViewAppearance != -1) {
                context.withAttrs(pitViewAppearance, R.styleable.PitViewAppearance) {
                    return PitViewAppearance(
                        lightInnerShadowColor = getColor(
                            R.styleable.PitViewAppearance_neuLightShadowColor,
                            pitColor.lighten(LIGHT_INNER_SHADOW_FRACTION)
                        ),
                        darkInnerShadowColor = getColor(
                            R.styleable.PitViewAppearance_neuDarkShadowColor,
                            pitColor.darken(DARK_INNER_SHADOW_FRACTION)
                        ),
                        softEdgeColor = getColor(
                            R.styleable.PitViewAppearance_softEdgeColor,
                            pitColor
                        ),
                        compatFillColor = getColor(
                            R.styleable.PitViewAppearance_neuCompatColor,
                            pitColor.darken(FILL_COLOR_FRACTION)
                        )
                    )
                }
            }

            return PitViewAppearance(pitColor)
        }
    }
}
