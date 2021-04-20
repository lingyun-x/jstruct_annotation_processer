package com.lingyun.lib.jstruct.protocol

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
class ProtocolPacketMatcher : IPacketMatcher {
    private val protocolNumberMatchers = HashMap<Int, IPacketMatcher>()

    override fun addPacketIndex(protocolNumber: Int, packetIndex: IPacketIndex) {
        var matcher = protocolNumberMatchers[protocolNumber]
        if (matcher == null) {
            matcher = PacketMatcher()
            protocolNumberMatchers[protocolNumber] = matcher
        }

        matcher.addPacketIndex(protocolNumber, packetIndex)
    }

    override fun getPacketClass(protocolNumber: Int, byteBuffer: ByteBuffer): Class<out IPacketable>? {
        for (pn in protocolNumber downTo -1) {
            val matcher = protocolNumberMatchers[pn]
            if (matcher != null) {
                val cla = matcher.getPacketClass(protocolNumber, byteBuffer)
                if (cla != null) {
                    return cla
                }
            }
        }
        return null
    }
}