package com.lingyun.lib.jstruct.protocol

import com.lingyun.lib.jstruct.annotation.ElementType
import java.nio.ByteBuffer


/*
* Created by mc_luo on 2021/3/29 .
* Copyright (c) 2021 The LingYun Authors. All rights reserved.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
data class PacketIndex(
    override val packet: Class<out IPacketable>,
    override val elementIndex: Int,
    override val elementType: ElementType,
    override val expect: String,
    override var dependice: IPacketIndex? = null
) : IPacketIndex {

    @ExperimentalUnsignedTypes
    override fun match(byteBuffer: ByteBuffer): Boolean {

        val match = when (elementType) {
            ElementType.INT8 -> {
                expect.toByte() == byteBuffer.get(elementIndex)
            }
            ElementType.UINT8 -> {
                expect.toShort() == byteBuffer.get(elementIndex).toUByte().toShort()
            }
            ElementType.CHAR -> {
                expect[0] == byteBuffer.getChar(elementIndex)
            }
            ElementType.INT16 -> {
                expect.toShort() == byteBuffer.getShort(elementIndex)
            }
            ElementType.UINT16 -> {
                expect.toInt() == byteBuffer.getShort(elementIndex).toUShort().toInt()
            }
            ElementType.INT32 -> {
                expect.toInt() == byteBuffer.getInt(elementIndex)
            }
            ElementType.UINT32 -> {
                expect.toLong() == byteBuffer.getInt(elementIndex).toUInt().toLong()
            }
            ElementType.LONG -> {
                expect.toLong() == byteBuffer.getLong(elementIndex)
            }
            ElementType.FLOAT -> {
                expect.toFloat() == byteBuffer.getFloat(elementIndex)
            }
            ElementType.DOUBLE -> {
                expect.toDouble() == byteBuffer.getDouble(elementIndex)
            }
            else -> {
                false
            }
        }

        if (match && dependice != null) {
            return dependice!!.match(byteBuffer)
        }

        return match
    }

    operator fun plus(other: PacketIndex): PacketIndex {
        var latest: IPacketIndex = this
        while (latest.dependice != null) {
            latest = latest.dependice!!
        }
        latest.dependice = other
        return this
    }


}
