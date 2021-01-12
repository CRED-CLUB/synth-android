/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.drawables

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation
import club.cred.synth.SynthUtils
import club.cred.synth.appearances.PitViewAppearance
import club.cred.synth.dp
import club.cred.synth.internals.PitHelper.Companion.DEFAULT_PRESSED_DEPTH
import club.cred.synth.internals.PitHelperInterface

@Suppress("LongParameterList") // acts as a builder
open class PitDrawable(
    pitViewAppearance: PitViewAppearance = PitViewAppearance(SynthUtils.defaultBaseColor),
    cornerRadiusArg: Float = 20f.dp,
    drawBackground: Boolean = true,
    drawShadows: Boolean = true,
    isSoft: Boolean = true,
    isCard: Boolean = true,
    pressedDepth: Float = DEFAULT_PRESSED_DEPTH,
    pitClipType: Int = NO_CLIP
) : Drawable() {

    var clipType: Int = NO_CLIP
        set(value) {
            field = value
            invalidateSelf()
        }

    var cornerRadius = cornerRadiusArg
        set(value) {
            field = value
            invalidateSelf()
        }

    private var buttonPitDrawable: PitHelperInterface = PitHelperInterface.newInstance(
        pitViewAppearance = pitViewAppearance,
        cornerRadius = cornerRadius,
        pressedDepth = pressedDepth,
        drawBackground = drawBackground,
        drawShadows = drawShadows,
        isSoft = isSoft,
        isCard = isCard
    )

    private val rect = Rect()

    init {
        clipType = pitClipType
    }

    override fun draw(canvas: Canvas) {
        val height = bounds.height()
        val width = bounds.width()
        when (clipType) {
            NO_CLIP -> buttonPitDrawable.draw(canvas, height, width)
            BOTTOM -> canvas.withClipAndTranslation {
                buttonPitDrawable.draw(this, height + 2 * cornerRadius.toInt(), width)
            }
            TOP -> canvas.withClipAndTranslation(0f, -2 * cornerRadius) {
                buttonPitDrawable.draw(this, height + 2 * cornerRadius.toInt(), width)
            }
            RIGHT -> canvas.withClipAndTranslation {
                buttonPitDrawable.draw(this, height, width + 2 * cornerRadius.toInt())
            }
            LEFT -> canvas.withClipAndTranslation(-2 * cornerRadius, 0F) {
                buttonPitDrawable.draw(this, height, width + 2 * cornerRadius.toInt())
            }
            LEFT_RIGHT -> canvas.withClipAndTranslation(-2 * cornerRadius, 0F) {
                buttonPitDrawable.draw(this, height, width + 4 * cornerRadius.toInt())
            }
            TOP_BOTTOM -> canvas.withClipAndTranslation(0f, -2 * cornerRadius) {
                buttonPitDrawable.draw(this, height + 4 * cornerRadius.toInt(), width)
            }
        }
    }

    private inline fun Canvas.withClipAndTranslation(tx: Float = 0f, ty: Float = 0f, block: Canvas.() -> Unit) {
        rect.set(0, 0, bounds.width(), bounds.height())
        withClip(rect) {
            if (tx == 0f && ty == 0f) { // if no tx, ty is supplied, ignore translation
                block()
            } else {
                withTranslation(tx, ty) { block() }
            }
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    companion object {
        const val NO_CLIP = 0
        const val TOP = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val RIGHT = 4
        const val LEFT_RIGHT = 5
        const val TOP_BOTTOM = 6
    }
}
