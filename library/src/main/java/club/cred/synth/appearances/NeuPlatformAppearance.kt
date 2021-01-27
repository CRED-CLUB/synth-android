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

data class NeuPlatformAppearance(
    val lightShadowColor: Int,
    val darkShadowColor: Int,
    val fillStartColor: Int,
    val fillEndColor: Int,
    val fillPressedStartColor: Int,
    val fillPressedEndColor: Int,
    val borderStartColor: Int,
    val borderEndColor: Int,
    val borderPressedStartColor: Int,
    val borderPressedEndColor: Int
) {

    constructor(platformColor: Int) : this(
        lightShadowColor = platformColor.lighten(LIGHT_SHADOW_FRACTION, DEFAULT_LIGHT_SHADOW_ALPHA),
        darkShadowColor = platformColor.darken(DARK_SHADOW_FRACTION, DEFAULT_DARK_SHADOW_ALPHA),
        fillStartColor = platformColor.lighten(FILL_START_COLOR_FRACTION),
        fillEndColor = platformColor.darken(FILL_END_COLOR_FRACTION),
        fillPressedStartColor = platformColor.darken(FILL_PRESSED_START_COLOR_FRACTION),
        fillPressedEndColor = platformColor.lighten(FILL_PRESSED_END_COLOR_FRACTION),
        borderStartColor = platformColor.lighten(BORDER_START_COLOR_FRACTION),
        borderEndColor = platformColor.darken(BORDER_END_COLOR_FRACTION),
        borderPressedStartColor = platformColor.darken(BORDER_PRESSED_START_COLOR_FRACTION),
        borderPressedEndColor = platformColor.darken(BORDER_PRESSED_END_COLOR_FRACTION)
    )

    companion object {

        private const val LIGHT_SHADOW_FRACTION = .5
        private const val DEFAULT_LIGHT_SHADOW_ALPHA = .3
        private const val DARK_SHADOW_FRACTION = .8
        private const val DEFAULT_DARK_SHADOW_ALPHA = 0.6
        private const val FILL_START_COLOR_FRACTION = .06
        private const val FILL_END_COLOR_FRACTION = .2
        private const val FILL_PRESSED_START_COLOR_FRACTION = .25
        private const val FILL_PRESSED_END_COLOR_FRACTION = .055
        private const val BORDER_START_COLOR_FRACTION = .065
        private const val BORDER_END_COLOR_FRACTION = .25
        private const val BORDER_PRESSED_START_COLOR_FRACTION = .25
        private const val BORDER_PRESSED_END_COLOR_FRACTION = .025

        fun TypedArray.getNeuPlatformAppearance(context: Context, platformColor: Int): NeuPlatformAppearance {
            val appearance = getResourceId(R.styleable.NeuButton_neuPlatformAppearance, -1)
            if (appearance != -1) {
                context.withAttrs(appearance, R.styleable.NeuPlatformAppearance) {
                    return NeuPlatformAppearance(
                        lightShadowColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuLightShadowColor,
                            platformColor.lighten(LIGHT_SHADOW_FRACTION, DEFAULT_LIGHT_SHADOW_ALPHA)
                        ),
                        darkShadowColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuDarkShadowColor,
                            platformColor.darken(DARK_SHADOW_FRACTION, DEFAULT_DARK_SHADOW_ALPHA)
                        ),
                        fillStartColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuFillStartColor,
                            platformColor.lighten(FILL_START_COLOR_FRACTION)
                        ),
                        fillEndColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuFillEndColor,
                            platformColor.darken(FILL_END_COLOR_FRACTION)
                        ),
                        fillPressedStartColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuFillPressedStartColor,
                            platformColor.darken(FILL_PRESSED_START_COLOR_FRACTION)
                        ),
                        fillPressedEndColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuFillPressedEndColor,
                            platformColor.lighten(FILL_PRESSED_END_COLOR_FRACTION)
                        ),
                        borderStartColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuBorderStartColor,
                            platformColor.lighten(BORDER_START_COLOR_FRACTION)
                        ),
                        borderEndColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuBorderEndColor,
                            platformColor.darken(BORDER_END_COLOR_FRACTION)
                        ),
                        borderPressedStartColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuBorderPressedStartColor,
                            platformColor.darken(BORDER_PRESSED_START_COLOR_FRACTION)
                        ),
                        borderPressedEndColor = getColor(
                            R.styleable.NeuPlatformAppearance_neuBorderPressedEndColor,
                            platformColor.darken(BORDER_PRESSED_END_COLOR_FRACTION)
                        )
                    )
                }
            }
            return NeuPlatformAppearance(platformColor)
        }
    }
}
