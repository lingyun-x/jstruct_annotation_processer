package com.lingyun.lib.jstrcut.annotation.processor

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

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
object TypeMirrorUtils {

    fun isBoolean(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.BOOLEAN
    }

    fun isBooleanBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Boolean"
    }

    fun isByte(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.BYTE
    }

    fun isByteBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Byte"
    }

    fun isShort(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.SHORT
    }

    fun isShortBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Short"
    }

    fun isChar(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.CHAR
    }

    fun isCharBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Character"
    }

    fun isInt(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.INT
    }

    fun isIntBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Integer"
    }

    fun isLong(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.LONG
    }

    fun isLongBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Long"
    }

    fun isFloat(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.FLOAT
    }

    fun isFloatBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Float"
    }

    fun isDouble(typeMirror: TypeMirror): Boolean {
        return typeMirror.kind == TypeKind.DOUBLE
    }

    fun isDoubleBox(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Double"
    }

    fun isString(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.String"
    }

    fun isBooleanArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "boolean[]"
    }

    fun isByteArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "byte[]"
    }

    fun isShortArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "short[]"
    }

    fun isCharArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "char[]"
    }

    fun isIntArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "int[]"
    }

    fun isLongArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "long[]"
    }

    fun isFloatArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "float[]"
    }

    fun isDoubleArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "double[]"
    }

    fun isStringArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.String[]"
    }

    fun isObjectArray(typeMirror: TypeMirror): Boolean {
        return typeMirror.toString() == "java.lang.Object[]"
    }

    fun getElementTypeClassString(element: Element): String {
        val nullable = element.annotationMirrors.firstOrNull() {
            it.annotationType.asElement().asType().toString() == "org.jetbrains.annotations.Nullable"
        } != null

//        element.annotationMirrors.forEach {
//            processingEnv?.messager?.printMessage(Diagnostic.Kind.NOTE,it.annotationType.asElement().asType().toString()+"\r\n")
//        }

        return if (nullable) {
            getTypeClassString(element.asType()) + "?"
        } else {
            getTypeClassString(element.asType())
        }
    }

    fun getTypeClassString(typeMirror: TypeMirror): String {
        return when {
            isBoolean(typeMirror) || isBooleanBox(typeMirror) -> {
                "Boolean"
            }
            isByte(typeMirror) || isByteBox(typeMirror) -> {
                "Byte"
            }
            isShort(typeMirror) || isShortBox(typeMirror) -> {
                "Short"
            }
            isChar(typeMirror) || isCharBox(typeMirror) -> {
                "Char"
            }
            isInt(typeMirror) || isIntBox(typeMirror) -> {
                "Int"
            }
            isLong(typeMirror) || isLongBox(typeMirror) -> {
                "Long"
            }
            isFloat(typeMirror) || isFloatBox(typeMirror) -> {
                "Float"
            }
            isDouble(typeMirror) || isDoubleBox(typeMirror) -> {
                "Double"
            }
            isString(typeMirror) -> {
                "String"
            }
            isBooleanArray(typeMirror) -> {
                "BooleanArray"
            }
            isByteArray(typeMirror) -> {
                "ByteArray"
            }
            isShortArray(typeMirror) -> {
                "ShortArray"
            }
            isCharArray(typeMirror) -> {
                "CharArray"
            }
            isIntArray(typeMirror) -> {
                "IntArray"
            }
            isLongArray(typeMirror) -> {
                "LongArray"
            }
            isFloatArray(typeMirror) -> {
                "FloatArray"
            }
            isDoubleArray(typeMirror) -> {
                "DoubleArray"
            }
            isStringArray(typeMirror) -> {
                "Array<String>"
            }
            typeMirror.kind == TypeKind.DECLARED -> {
                val typeString = typeMirror.toString()
                typeString
            }
            else -> {
                throw IllegalArgumentException("not support this typeMirror:${typeMirror}")
            }
        }
    }
}
