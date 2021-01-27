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
import club.cred.synth.SynthUtils
import club.cred.synth.darken
import club.cred.synth.lighten
import club.cred.synth.withAttrs

data class NeuButtonIconAppearance(
    val fillStartColor: Int,
    val fillEndColor: Int,
    val fillPressedStartColor: Int,
    val fillPressedEndColor: Int,
    val compatColor: Int,
) {

    constructor(buttonColor: Int, buttonType: Int) : this(
        fillStartColor = if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
            buttonColor.darken(ELEVATED_FLAT_START_COLOR_FRACTION)
        } else {
            buttonColor.darken(ELEVATED_SOFT_START_COLOR_FRACTION)
        },
        fillEndColor = if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
            buttonColor.darken(ELEVATED_FLAT_END_COLOR_FRACTION)
        } else {
            buttonColor.lighten(ELEVATED_SOFT_END_COLOR_FRACTION)
        },
        fillPressedStartColor = if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
            buttonColor.darken(ELEVATED_FLAT_PRESSED_START_COLOR_FRACTION)
        } else {
            buttonColor.darken(ELEVATED_SOFT_PRESSED_START_COLOR_FRACTION)
        },
        fillPressedEndColor = if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
            buttonColor.darken(ELEVATED_FLAT_PRESSED_END_COLOR_FRACTION)
        } else {
            buttonColor.lighten(ELEVATED_SOFT_PRESSED_END_COLOR_FRACTION)
        },
        compatColor = buttonColor.lighten(COMPAT_COLOR_FRACTION)
    )

    companion object {
        private const val ELEVATED_FLAT_START_COLOR_FRACTION = .28
        private const val ELEVATED_SOFT_START_COLOR_FRACTION = .03
        private const val ELEVATED_FLAT_END_COLOR_FRACTION = .05
        private const val ELEVATED_SOFT_END_COLOR_FRACTION = .03
        private const val ELEVATED_FLAT_PRESSED_START_COLOR_FRACTION = .45
        private const val ELEVATED_SOFT_PRESSED_START_COLOR_FRACTION = .40
        private const val ELEVATED_FLAT_PRESSED_END_COLOR_FRACTION = .06
        private const val ELEVATED_SOFT_PRESSED_END_COLOR_FRACTION = .03
        private const val COMPAT_COLOR_FRACTION = .064

        fun TypedArray.getNeuButtonIconAppearance(context: Context, buttonType: Int, buttonColor: Int): NeuButtonIconAppearance {
            val appearance = getResourceId(R.styleable.NeuButton_neuButtonIconAppearance, -1)
            if (appearance != -1) {
                context.withAttrs(appearance, R.styleable.NeuButtonIconAppearance) {
                    return NeuButtonIconAppearance(
                        fillStartColor = getColor(
                            R.styleable.NeuButtonIconAppearance_neuFillStartColor,
                            if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
                                buttonColor.darken(ELEVATED_FLAT_START_COLOR_FRACTION)
                            } else {
                                buttonColor.darken(ELEVATED_SOFT_START_COLOR_FRACTION)
                            }
                        ),
                        fillEndColor = getColor(
                            R.styleable.NeuButtonIconAppearance_neuFillEndColor,
                            if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
                                buttonColor.darken(ELEVATED_FLAT_END_COLOR_FRACTION)
                            } else {
                                buttonColor.lighten(ELEVATED_SOFT_END_COLOR_FRACTION)
                            }
                        ),
                        fillPressedStartColor = getColor(
                            R.styleable.NeuButtonIconAppearance_neuFillPressedStartColor,
                            if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
                                buttonColor.darken(ELEVATED_FLAT_PRESSED_START_COLOR_FRACTION)
                            } else {
                                buttonColor.darken(ELEVATED_SOFT_PRESSED_START_COLOR_FRACTION)
                            }
                        ),
                        fillPressedEndColor = getColor(
                            R.styleable.NeuButtonIconAppearance_neuFillPressedEndColor,
                            if (buttonType == SynthUtils.TYPE_ELEVATED_FLAT) {
                                buttonColor.darken(ELEVATED_FLAT_PRESSED_END_COLOR_FRACTION)
                            } else {
                                buttonColor.lighten(ELEVATED_SOFT_PRESSED_END_COLOR_FRACTION)
                            }
                        ),
                        compatColor = getColor(
                            R.styleable.NeuButtonIconAppearance_neuButtonCompatColor,
                            buttonColor.lighten(COMPAT_COLOR_FRACTION)
                        )
                    )
                }
            }

            return NeuButtonIconAppearance(buttonColor, buttonType)
        }
    }
}
