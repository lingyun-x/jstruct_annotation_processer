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
interface IPacketIndex : Comparable<IPacketIndex> {
    val byteIndex: Int
    val elementType: ElementType
    val expect: Any
    var dependice: IPacketIndex?
    fun match(byteBuffer: ByteBuffer): Boolean
    fun match(index: IPacketIndex): Boolean

    fun deps(): Int {
        var deps = 1
        var dep = dependice
        while (dep != null) {
            deps++
            dep = dep.dependice
        }
        return deps
    }

    override fun compareTo(other: IPacketIndex): Int {
        return other.deps() - deps()
    }
}