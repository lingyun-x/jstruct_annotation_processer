package com.lingyun.lib.jstruct.protocol

import com.lingyun.lib.jstruct.JStruct
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
object JStrcutProtocol : IPacketMatcher {


    private val packetMatcher = ProtocolPacketMatcher()

    override fun addPacketIndex(protocolNumber: Int, packetIndex: IPacketIndex) {
        packetMatcher.addPacketIndex(protocolNumber, packetIndex)
    }

    override fun getPacketClass(protocolNumber: Int, byteBuffer: ByteBuffer): Class<out IPacketable>? {
        return packetMatcher.getPacketClass(protocolNumber, byteBuffer)
    }

    fun unpack(data: ByteArray): IPacketable? {
        val byteBuffer = ByteBuffer.wrap(data)
        var packetable = getPacketClass(0, byteBuffer)
        if (packetable == null) {
            packetable = UnknowPacket::class.java
        }
        val packet = packetable.newInstance()

        val elements = JStruct.unpack(packet.packetStruct(), data)
        packet.applyElements(elements)
        return packet
    }

    fun pack(packet: IPacketable): ByteArray {
        return JStruct.pack(packet.packetStruct(), packet.elements())
    }

}