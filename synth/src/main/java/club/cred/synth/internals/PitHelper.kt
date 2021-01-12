/*
 * Copyright 2020 Dreamplug Technologies Private Limited
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package club.cred.synth.internals

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import androidx.core.graphics.withClip
import androidx.core.graphics.withTranslation
import club.cred.synth.appearances.PitViewAppearance
import club.cred.synth.dp

@Suppress("LongParameterList") // acts as a builder
class PitHelper(
    pitViewAppearance: PitViewAppearance,
    cornerRadius: Float,
    private val pressedDepth: Float,
    private val drawShadows: Boolean,
    private val isSoft: Boolean,
    private val isCard: Boolean
) : PitHelperInterface {

    private val lightInnerShadow: BlurHelper = BlurHelper(
        blur = pressedDepth / 2,
        radius = cornerRadius,
        shadowColor = pitViewAppearance.lightInnerShadowColor,
        onlyBorder = true
    )
    private val darkInnerShadow: BlurHelper = BlurHelper(
        blur = pressedDepth,
        radius = cornerRadius,
        shadowColor = pitViewAppearance.darkInnerShadowColor,
        onlyBorder = true
    )

    //    private val pitDrawable: BlurHelper = BlurHelper(0f, cornerRadius)
    private val softEdge: BlurHelper = BlurHelper(
        blur = 1.5f.dp,
        radius = cornerRadius,
        shadowColor = pitViewAppearance.softEdgeColor,
        onlyBorder = true
    )

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            lightInnerShadow.radius = field
            darkInnerShadow.radius = field
//            pitDrawable.radius = field
            softEdge.radius = field
        }

    init {
        this.cornerRadius = cornerRadius
        darkInnerShadow.strokeWidth = pressedDepth
        lightInnerShadow.strokeWidth = pressedDepth
        softEdge.strokeWidth = 2f.dp
    }

    private val path = Path()
    private val rectF = RectF()

    override fun draw(canvas: Canvas, height: Int, width: Int) {
        val heightF = height.toFloat()
        val widthF = width.toFloat()
        if (rectF.width() != widthF || rectF.height() != heightF) {
            rectF.set(0f, 0f, widthF, heightF)
            path.reset()
            path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
        }

        canvas.withClip(path) {
//            if (drawBackground) {
//                pitDrawable.draw(canvas, height, width)
//            }
            if (drawShadows) {
                val tx = -pressedDepth * 1.5f
                val ty = if (isCard) -pressedDepth * 1.5f else -pressedDepth * 1.1f

                withTranslation(tx, ty) {
                    lightInnerShadow.draw(canvas, (height + pressedDepth * 1.5f).toInt(), (width + pressedDepth * 1.5f).toInt())
                }
                darkInnerShadow.draw(canvas, (height + pressedDepth * 1.5f).toInt(), (width + pressedDepth * 1.5f).toInt())
            }
        }
        if (isSoft && drawShadows) {
            softEdge.draw(canvas, height, width)
        }
    }

    companion object {
        val DEFAULT_PRESSED_DEPTH = 10f.dp
    }
}
