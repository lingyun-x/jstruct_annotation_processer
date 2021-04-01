package com.lingyun.sample

import com.lingyun.lib.jstruct.annotation.StructAnnotation

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
class BasicEmbedInfo {
    var aByte: Byte = 0x01

    @StructAnnotation("1B")
    var aUByte: Short = 0xff

    var aShort: Short = 0x0102

    @StructAnnotation("1H")
    var aUShort: Int = 0xff01
    var aInt: Int = 0x01020304

    @StructAnnotation("1I")
    var aUInt: Long = 0xff020304
    var aLong: Long = 0x0102030405060708
    var aFloat: Float = 0.1f
    var aDouble: Double = 0.2
}