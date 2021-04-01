package com.lingyun.lib.jstruct.protocol

import java.nio.ByteBuffer
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
class PacketMatcher : IPacketMatcher {
    val matcherMap = HashMap<Int, MutableList<IPacketIndex>>()

    override fun addPacketIndex(
        protocolNumber: Int,
        packetIndex: IPacketIndex
    ) {
        var index: IPacketIndex? = packetIndex

        while (index != null) {
            var packetIndexs = matcherMap.get(packetIndex.elementIndex)
            if (packetIndexs == null) {
                packetIndexs = ArrayList<IPacketIndex>()
                matcherMap.put(packetIndex.elementIndex, packetIndexs)
            }

            packetIndexs.add(packetIndex)
            packetIndexs.sort()

            index = index.dependice
        }

    }

    override fun getPacketClass(protocolNumber: Int, byteBuffer: ByteBuffer): Class<out IPacketable>? {
        val keys = matcherMap.keys.sorted()
        for (key in keys) {
            val indexs = matcherMap[key]!!

            for (index in indexs) {
                if (index.match(byteBuffer)) {
                    return index.packet
                }
            }

        }
        return null
    }
}