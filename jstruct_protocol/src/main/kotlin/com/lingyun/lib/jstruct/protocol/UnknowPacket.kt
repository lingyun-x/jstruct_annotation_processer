package com.lingyun.lib.jstruct.protocol

/*
* Created by mc_luo on 2021/4/1 .
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
class UnknowPacket : IPacketable {
    var body: ByteArray = ByteArray(0)

    override fun packetStruct(): String {
        return "${body.size}[b]"
    }

    override fun elements(): List<Any> {
        return listOf(body)
    }

    override fun applyElements(elements: List<Any>): Int {
        body = elements[0] as ByteArray
        return 1
    }
}