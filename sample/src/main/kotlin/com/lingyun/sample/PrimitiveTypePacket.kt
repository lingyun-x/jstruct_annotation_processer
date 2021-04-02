package com.lingyun.sample

import com.lingyun.lib.jstruct.annotation.*
import com.lingyun.lib.jstruct.protocol.IPacketable

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
@ProtocolAnnotation(
    protocolNumber = 1, customSerialization = false, byteIndexs = [
        ByteIndex(
            2, elementType = ElementType.INT32, elementValue = MessageType.BASIC_TYPE.toString()
        )
    ]
)
abstract class PrimitiveTypePacket : IPacketable {
    var head: PacketHead = PacketHead()

    var aByte: Byte = 0

    @StructAnnotation("1B")
    var aUByte: Short = 0

    var aShort: Short = 0

    @StructAnnotation("1H")
    var aUShort: Int = 0

    var aInt: Int = 0

    @StructAnnotation("1I")
    var aUInt: Long = 0L

    @StructAnnotation("1l")
    var aLong: Long = 0L
    var aFloat: Float = 0.0f
    var aDouble: Double = 0.0

    var byteArrayLen: Int = 2

    @StructAnnotation("@-1[b]")
    var byteArray: ByteArray = byteArrayOf(0x01, 0x02)

    var shortArrayLen: Int = 2

    @StructAnnotation("@-1[h]")
    var shortArray: ShortArray = shortArrayOf(0x7f01, 0x7f02)

    var intArrayLen: Int = 2

    @StructAnnotation("@-1[i]")
    var intArray: IntArray = intArrayOf(0x01020304, 0x07060504)

    var longArrayLen: Int = 2

    @StructAnnotation("@-1[l]")
    var longArray: LongArray = longArrayOf(0x0102030405060708, 0x0807060504030201)

    var floatArrayLen: Int = 2

    @StructAnnotation("@-1[f]")
    var floatArray: FloatArray = floatArrayOf(0.2f, 0.3f)

    var doubleArrayLen: Int = 2

    @StructAnnotation("@-1[d]")
    var doubleArray: DoubleArray = doubleArrayOf(0.1, 0.2)


}