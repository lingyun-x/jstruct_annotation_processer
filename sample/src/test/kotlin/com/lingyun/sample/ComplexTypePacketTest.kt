package com.lingyun.sample

import com.eav.gs.online.lib.protocol.PacketProtocols
import com.lingyun.lib.jstruct.HexUtil
import com.lingyun.lib.jstruct.protocol.JStrcutProtocol
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/*
* Created by mc_luo on 2021/4/2 .
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
internal class ComplexTypePacketTest {


    @Test
    fun testComplexPacket() {
        PacketProtocols.initProtocols()
        val complexTypePacket = ComplexTypePacketImpl().also {
            it.packetHead.messageType = MessageType.COMPLEX_TYPE
            it.complexArrayLen = 2
            it.complextData = Array(2) {
                BasicEmbedInfo()
            }

            it.complext2 = BasicEmbedInfo()
        }

        val bytes = JStrcutProtocol.pack(complexTypePacket)
        println("bytes:${HexUtil.bytesToHexSpace(bytes)}")

        val unpackPacket = JStrcutProtocol.unpack(bytes) as ComplexTypePacket

        val unpackBytes = JStrcutProtocol.pack(unpackPacket)
        assert(bytes.contentEquals(unpackBytes))

    }

    fun testEmbedArrayPacket(){
        PacketProtocols.initProtocols()
//        val arrayPacket = EmbedArrayPacket()
    }
}