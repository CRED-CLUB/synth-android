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
import club.cred.synth.appearances.DividerAppearance
import club.cred.synth.appearances.DividerAppearance.Companion.getDividerAppearance
import club.cred.synth.drawables.DividerDrawable
import club.cred.synth.dynamicAttr
import club.cred.synth.withAttrs

class DividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var drawShadows: Boolean by dynamicAttr(true, this::updateBackground)
    var neuDividerColor: Int by dynamicAttr(DEFAULT_COLOR) {
        dividerAppearance = DividerAppearance(neuDividerColor)
    }
    var dividerAppearance: DividerAppearance by dynamicAttr(
        DividerAppearance(DEFAULT_COLOR), this::updateBackground
    )

    init {
        withAttrs(attrs, R.styleable.DividerView) {
            drawShadows = getBoolean(R.styleable.DividerView_drawShadows, drawShadows)
            neuDividerColor = getColor(R.styleable.DividerView_dividerColor, neuDividerColor)
            dividerAppearance = getDividerAppearance(context, neuDividerColor)
        }

        updateBackground()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun <T> updateBackground(t: T) {
        updateBackground()
    }

    private fun updateBackground() {
        background = DividerDrawable(drawShadows, dividerAppearance)
    }

    companion object {
        private const val DEFAULT_COLOR = 0xFF2e3234.toInt()
    }
}
