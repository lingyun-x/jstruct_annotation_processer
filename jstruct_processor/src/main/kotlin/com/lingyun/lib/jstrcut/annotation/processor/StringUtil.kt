package com.lingyun.lib.jstrcut.annotation.processor

/*
* Created by mc_luo on 2021/3/31 .
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
object StringUtil {
    val UP_START = 'A'
    val UP_END = 'Z'
    val LOW_START = 'a'
    val LOW_END = 'z'


    @ExperimentalStdlibApi
    fun replaceFirstCharToLow(letter: String): String {
        if (letter.isEmpty()) {
            throw IllegalAccessError("string is empty")
        }

        if (letter[0] in UP_START..UP_END) {
            val firstChar: Char = (letter[0].toInt() + LOW_START.toInt() - UP_START.toInt()).toChar()
            return letter.replaceFirstChar {
                firstChar
            }
        } else if (letter[0] in LOW_START..LOW_END) {
            return letter
        }
        throw IllegalAccessError("string is first char is not in[A-Z] or not in [a-z]")
    }

    @ExperimentalStdlibApi
    fun replaceFirstCharToUp(letter: String): String {
        if (letter.isEmpty()) {
            throw IllegalAccessError("string is empty")
        }

        if (letter[0] in UP_START..UP_END) {
            return letter
        } else if (letter[0] in LOW_START..LOW_END) {
            val firstChar: Char = (letter[0].toInt() - LOW_START.toInt() + UP_START.toInt()).toChar()
            return letter.replaceFirstChar {
                firstChar
            }
        }
        throw IllegalAccessError("string is first char is not in[A-Z] or not in [a-z]")
    }
}