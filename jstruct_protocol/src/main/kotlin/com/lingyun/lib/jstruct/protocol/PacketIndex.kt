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
    override val byteIndex: Int,
    override val elementType: ElementType,
    override val expect: String,
    override var dependice: IPacketIndex? = null
) : IPacketIndex {

    @ExperimentalUnsignedTypes
    override fun match(byteBuffer: ByteBuffer): Boolean {

        val match = when (elementType) {
            ElementType.INT8 -> {
                expect.toByte() == byteBuffer.get(byteIndex)
            }
            ElementType.UINT8 -> {
                expect.toShort() == byteBuffer.get(byteIndex).toUByte().toShort()
            }
            ElementType.CHAR -> {
                expect[0] == byteBuffer.getChar(byteIndex)
            }
            ElementType.INT16 -> {
                expect.toShort() == byteBuffer.getShort(byteIndex)
            }
            ElementType.UINT16 -> {
                expect.toInt() == byteBuffer.getShort(byteIndex).toUShort().toInt()
            }
            ElementType.INT32 -> {
                expect.toInt() == byteBuffer.getInt(byteIndex)
            }
            ElementType.UINT32 -> {
                expect.toLong() == byteBuffer.getInt(byteIndex).toUInt().toLong()
            }
            ElementType.LONG -> {
                expect.toLong() == byteBuffer.getLong(byteIndex)
            }
            ElementType.FLOAT -> {
                expect.toFloat() == byteBuffer.getFloat(byteIndex)
            }
            ElementType.DOUBLE -> {
                expect.toDouble() == byteBuffer.getDouble(byteIndex)
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

    override fun match(index: IPacketIndex): Boolean {
        if (index == this) return true

        val match = index.byteIndex == byteIndex && elementType == index.elementType && expect == index.expect

        if (!match) return false

        if (index.dependice == dependice) {
            return true
        }

        if (index.dependice != null && dependice != null) {
            return dependice!!.match(index.dependice!!)
        }

        return false
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
