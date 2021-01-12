/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal inline fun View.withAttrs(set: AttributeSet?, attrs: IntArray, func: TypedArray.() -> Unit) {
    val a = context.theme.obtainStyledAttributes(set, attrs, 0, 0)
    try {
        a.func()
    } finally {
        a.recycle()
    }
}

internal inline fun Context.withAttrs(resId: Int, attrs: IntArray, func: TypedArray.() -> Unit) {
    val a = theme.obtainStyledAttributes(resId, attrs)
    try {
        a.func()
    } finally {
        a.recycle()
    }
}

internal inline fun View.withAttrs(resId: Int, attrs: IntArray, func: TypedArray.() -> Unit) {
    val a = context.theme.obtainStyledAttributes(resId, attrs)
    try {
        a.func()
    } finally {
        a.recycle()
    }
}

internal inline fun <T> View.dynamicAttr(
    initialValue: T,
    crossinline onChange: (T) -> Unit = {}
): ReadWriteProperty<Any?, T> {
    return object : ObservableProperty<T>(initialValue) {

        var initDone = false

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            if (!initDone) {
                initDone = true
                return
            }
            onChange(newValue)
            invalidate()
            requestLayout()
        }
    }
}

internal fun TypedArray.getDrawableSafe(context: Context, @StyleableRes res: Int): Drawable? =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        val resourceId = getResourceId(res, -1)
        if (resourceId != -1) AppCompatResources.getDrawable(context, resourceId) else null
    } else {
        getDrawable(res)
    }

internal val Float.dp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)
    }

internal val Float.sp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, displayMetrics)
    }

internal val Int.dp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics)
    }

internal val Int.sp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), displayMetrics)
    }

internal val Int.twoDigit: String
    get() {
        return String.format(Locale.ENGLISH, "%02d", this)
    }

internal fun Int.lighten(@FloatRange(from = 0.0, to = 1.0) fraction: Double, overRideAlpha: Double? = null): Int {
    var red = Color.red(this)
    var green = Color.green(this)
    var blue = Color.blue(this)
    red = lightenColor(red, fraction)
    green = lightenColor(green, fraction)
    blue = lightenColor(blue, fraction)
    val alpha = if (overRideAlpha != null) {
        floatToInt(overRideAlpha)
    } else {
        Color.alpha(this)
    }
    return Color.argb(alpha, red, green, blue)
}

internal fun Int.darken(@FloatRange(from = 0.0, to = 1.0) fraction: Double, overRideAlpha: Double? = null): Int {
    var red = Color.red(this)
    var green = Color.green(this)
    var blue = Color.blue(this)
    red = darkenColor(red, fraction)
    green = darkenColor(green, fraction)
    blue = darkenColor(blue, fraction)
    val alpha = if (overRideAlpha != null) {
        floatToInt(overRideAlpha)
    } else {
        Color.alpha(this)
    }
    return Color.argb(alpha, red, green, blue)
}

internal fun darkenColor(color: Int, fraction: Double): Int {
    return max(color - color * fraction, 0.0).toInt()
}

internal fun floatToInt(float: Double): Int {
    val sanitized = max(0.0, min(float, 1.0))
    return (sanitized * MAX_HEX).toInt()
}

internal fun lightenColor(color: Int, fraction: Double): Int {
    return min(color + (MAX_HEX - color) * fraction, MAX_HEX.toDouble()).toInt()
}

const val MAX_HEX = 255
