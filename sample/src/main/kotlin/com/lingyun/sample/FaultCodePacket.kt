package com.lingyun.sample

import com.lingyun.lib.jstruct.annotation.ByteIndex
import com.lingyun.lib.jstruct.annotation.ElementType
import com.lingyun.lib.jstruct.annotation.ProtocolAnnotation
import com.lingyun.lib.jstruct.annotation.StructAnnotation

/*
* Created by mc_luo on 2021/4/20 .
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
    protocolNumber = 1, byteIndexs = [ByteIndex(
        byteIndex = 0, elementType = ElementType.INT32, elementValue = "1"
    )]
)
abstract class FaultCodePacket : BasePacket() {
    var missionId: Long = 0
    var numberOfFaultCode: Int = 0

    @StructAnnotation("(@-1)*2s")
    var faultCodes: String = ""
}