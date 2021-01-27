/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.helper

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Path
import android.graphics.Region
import android.graphics.Shader
import android.os.Build
import android.text.TextPaint
import android.widget.TextView
import androidx.annotation.RequiresApi

/**
 * clips everything inside the rect.
 */
@RequiresApi(Build.VERSION_CODES.O)
inline fun Canvas.withClipOut(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    clipOutRect(left, top, right, bottom)
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}

/**
 * clips everything inside the path.
 */
@Suppress("DEPRECATION")
inline fun Canvas.withClipOut(
    path: Path,
    block: Canvas.() -> Unit
) {
    val checkpoint = save()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        clipOutPath(path)
    } else {
        clipPath(path, Region.Op.DIFFERENCE)
    }
    try {
        block()
    } finally {
        restoreToCount(checkpoint)
    }
}

fun TextView.setGradientTextColors(vararg colors: Int) {
    setTextColor(colors[0])
    val paint: TextPaint = paint
    val width: Float = paint.measureText(text, 0, text.length)

    val textShader: Shader = LinearGradient(0f, textSize / 2, width, textSize / 2, colors, null, Shader.TileMode.CLAMP)
    paint.shader = textShader
}
