package com.lingyun.sample

import com.lingyun.lib.jstruct.protocol.IPacketable

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
abstract class BasePacket :IPacketable{
    //uint32
    var messageType: Int = 0

    //uint32
    var messageNumber: Int = 0

    //uint32
    var uavId: Int = 0

    //uint32 UAV time since boot up. Unit is 100ms. Timestamp is the report time.
    var timestamp: Int = 0

    //uint32 Time of day. Hour, minute, second, and 0.1second of Greenwich time. 32 bits will contain YYMMDDHHMMSS.S
    var timeOfDay: Int = 0

}