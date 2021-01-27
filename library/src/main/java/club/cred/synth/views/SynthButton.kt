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
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import club.cred.synth.appearances.NeuButtonIconAppearance
import club.cred.synth.appearances.NeuFlatButtonAppearance
import club.cred.synth.appearances.NeuPlatformAppearance
import club.cred.synth.drawables.ButtonIconContainerDrawable
import club.cred.synth.internals.SynthButtonDelegate

open class SynthButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val delegate = SynthButtonDelegate(this)

    // Common Properties
    var platformColor: Int by delegate::platformColor
    var flatButtonColor: Int by delegate::flatButtonColor
    var compatColor: Int by delegate::compatColor
    var cornerRadius: Float by delegate::cornerRadius
    var neuPlatformAppearance: NeuPlatformAppearance by delegate::neuPlatformAppearance

    // Only for TYPE_ELEVATED_FLAT
    var neuFlatButtonAppearance: NeuFlatButtonAppearance? by delegate::neuFlatButtonAppearance

    // Properties specific to NeuButton
    var iconPitRadius: Float by delegate::iconPitRadius
    var buttonIconDrawable: Drawable? by delegate::buttonIconDrawable
    var neuButtonIconAppearance: NeuButtonIconAppearance? by delegate::neuButtonIconAppearance

    init {
        delegate.handleAttrs(attrs)

        delegate.buttonIconDrawable?.let { setButtonDrawable(it) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        delegate.onTouchEvent(event)
        Log.d("ss", ":a")
        return super.onTouchEvent(event)
    }

    fun setButtonDrawable(drawable: Drawable) {
        val buttonIconContainerDrawable = ButtonIconContainerDrawable(
            appearance = delegate.neuButtonIconAppearance!!,
            iconPitRadius = delegate.iconPitRadius,
            type = delegate.buttonType,
            drawable = drawable
        )
        val pitRadius = delegate.iconPitRadius.toInt() * 2

        buttonIconContainerDrawable.bounds = Rect(0, 0, pitRadius, pitRadius)
        setCompoundDrawables(buttonIconContainerDrawable, null, null, null)
    }

    fun removeButtonIcon() {
        setCompoundDrawables(null, null, null, null)
    }
}
