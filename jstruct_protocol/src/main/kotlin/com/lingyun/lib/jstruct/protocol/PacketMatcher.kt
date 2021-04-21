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
class PacketMatcher : IPacketMatcher {
    val matcherMap = HashMap<Int, MutableList<PacketElementIndex>>()

    override fun addPacketIndex(
        protocolNumber: Int,
        packet: Class<out IPacketable>,
        packetIndex: IPacketIndex
    ) {
        var index: IPacketIndex? = packetIndex

        while (index != null) {
            var packetIndexs = matcherMap.get(packetIndex.byteIndex)
            if (packetIndexs == null) {
                packetIndexs = ArrayList<PacketElementIndex>()
                matcherMap.put(packetIndex.byteIndex, packetIndexs)
            }

            packetIndexs.add(PacketElementIndex(packet, packetIndex))
            index = index.dependice
        }

    }

    override fun getPacketClass(protocolNumber: Int, byteBuffer: ByteBuffer): Class<out IPacketable>? {
        val keys = matcherMap.keys.sorted()

        for (i in keys.size-1 downTo 0) {
            val protocolIndex = matcherMap[keys[i]]!!

            for (elementIndex in protocolIndex) {
                if (elementIndex.packetIndex.match(byteBuffer)) {
                    return elementIndex.packet
                }
            }

        }
        return null
    }

    override fun getPacketClass(protocolNumber: Int, packetIndex: IPacketIndex): Class<out IPacketable>? {
        val keys = matcherMap.keys.sorted()
        for (i in keys.size-1 downTo 0) {
            val protocolIndex = matcherMap[keys[i]]!!

            for (elementIndex in protocolIndex) {
                if (elementIndex.packetIndex.match(packetIndex)) {
                    return elementIndex.packet
                }
            }
        }
        return null
    }

    data class PacketElementIndex(val packet: Class<out IPacketable>, val packetIndex: IPacketIndex) :
        Comparable<PacketElementIndex> {

        override fun compareTo(other: PacketElementIndex): Int {
            return packetIndex.compareTo(other.packetIndex)
        }
    }
}