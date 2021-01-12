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

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import club.cred.synth.appearances.NeuFlatButtonAppearance
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.internals.SynthButtonDelegate

open class SynthImageButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val delegate = SynthButtonDelegate(this)

    // Common Properties
    var platformColor: Int by delegate::platformColor
    var flatButtonColor: Int by delegate::flatButtonColor
    var compatColor: Int by delegate::compatColor
    var cornerRadius: Float by delegate::cornerRadius
    var neuPlatformAppearance: NeuPlatformAppearance by delegate::neuPlatformAppearance

    // Only for TYPE_ELEVATED_FLAT
    var neuFlatButtonAppearance: NeuFlatButtonAppearance? by delegate::neuFlatButtonAppearance

    init {
        delegate.handleAttrs(attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        delegate.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}
