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
import club.cred.synth.withAttrs

data class DividerAppearance(
    val neuDividerStartColor: Int,
    val neuDividerMiddleColor: Int,
    val neuDividerEndColor: Int
) {
    constructor(color: Int) : this(color, color, color)

    val gradientColors: IntArray =
        if (SynthUtils.isCompatDevice()) {
            intArrayOf(neuDividerMiddleColor, neuDividerMiddleColor)
        } else {
            intArrayOf(neuDividerStartColor, neuDividerMiddleColor, neuDividerEndColor)
        }

    val gradientPositions: FloatArray =
        if (SynthUtils.isCompatDevice()) {
            floatArrayOf(0f, 1f)
        } else {
            floatArrayOf(0f, HALF_FRACTION, 1f)
        }

    companion object {

        const val HALF_FRACTION = 0.5f

        fun TypedArray.getDividerAppearance(context: Context, dividerColor: Int): DividerAppearance {
            val dividerAppearance = getResourceId(R.styleable.DividerView_dividerAppearance, -1)
            if (dividerAppearance != -1) {
                context.withAttrs(dividerAppearance, R.styleable.DividerAppearance) {
                    return DividerAppearance(
                        neuDividerStartColor = getColor(
                            R.styleable.DividerAppearance_neuDividerStartColor,
                            dividerColor
                        ),
                        neuDividerMiddleColor = getColor(
                            R.styleable.DividerAppearance_neuDividerMiddleColor,
                            dividerColor
                        ),
                        neuDividerEndColor = getColor(
                            R.styleable.DividerAppearance_neuDividerEndColor,
                            dividerColor
                        )
                    )
                }
            }

            return DividerAppearance(dividerColor)
        }
    }
}
