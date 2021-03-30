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
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import club.cred.synth.helper.addParallelogram
import club.cred.synth.internals.NeuPlatformHelper

class ParallelogramLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    val cornerRad = NeuPlatformHelper.DEFAULT_PATH_RAD
    var angle: Double = 10.0
    val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addParallelogram(h, w, angle, cornerRad)
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val count = canvas.save()
        canvas.clipPath(path)
        val result = super.drawChild(canvas, child, drawingTime)
        canvas.restoreToCount(count)
        return result
    }
}
