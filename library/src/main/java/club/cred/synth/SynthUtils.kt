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

import android.os.Build

object SynthUtils {

    var forceNSB: Boolean = false

    // will use snapshot in isCompatDevice(). so value doesn't change in an app session.
    private val forceNsbSnapshot by lazy { forceNSB }

    fun isCompatDevice(): Boolean = !isDeviceSupported() || forceNsbSnapshot

    private fun isDeviceSupported() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    internal const val TYPE_ELEVATED_SOFT = 2
    internal const val TYPE_ELEVATED_FLAT = 3

    private const val PLATFORM_GAP = 5f
    val platformGap = PLATFORM_GAP.dp

    const val defaultBaseColor = 0xFF212425.toInt()
}
