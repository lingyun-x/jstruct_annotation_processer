package com.lingyun.sample

import com.lingyun.lib.jstruct.annotation.*
import com.lingyun.lib.jstruct.protocol.IPacketable

/*
* Created by mc_luo on 2021/3/30 .
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
            2, elementType = ElementType.INT32, elementValue = MessageType.COMPLEX_TYPE.toString()
        )
    ]
)
abstract class ComplexTypePacket : IPacketable {
    @Embed
    var packetHead: PacketHead = PacketHead()

    var complexArrayLen: Int = 2

    @StructAnnotation("@-1[?]")
    @Embed
    var complextData: Array<BasicEmbedInfo> = Array(2) {
        BasicEmbedInfo()
    }

    var complexArrayLen2 :Int = 1

    @StructAnnotation("@-1[?]")
    var complextData2: Array<BasicEmbedInfo> = Array(1) {
        BasicEmbedInfo()
    }

    var aByte: Byte = 0

    @Embed
    var complext2: BasicEmbedInfo = BasicEmbedInfo()

    @Embed
    var complext3: BasicEmbedInfo = BasicEmbedInfo()
}