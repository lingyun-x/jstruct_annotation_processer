package com.lingyun.lib.jstrcut.annotation.processor

import com.lingyun.lib.jstruct.annotation.ElementIndex
import com.lingyun.lib.jstruct.annotation.ElementType
import com.lingyun.lib.jstruct.annotation.ProtocolAnnotation
import com.sun.tools.javac.code.Attribute
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

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
object ProtocolAnnotationMirrorUtils {

    fun parse(
        classElement: TypeElement,
        processingEnv: ProcessingEnvironment
    ): ProtocolAnnotationData {
        val annotationMirrors = classElement.annotationMirrors

        val protocolElement: Element = processingEnv.elementUtils.getTypeElement(
            ProtocolAnnotation::class.java.name
        )

        val protocolType = protocolElement.asType()
        val protocolMirror = annotationMirrors.first { it.annotationType == protocolType }

        val typeValueElement = processingEnv.elementUtils.getTypeElement(
            ElementIndex::class.java.name
        )
        val typeValueType = typeValueElement.asType()

        var protocolNumber: Int = 0
        var customSerialization: Boolean = false
        var elementValues: Array<ElementIndexData>? = null

        protocolMirror.elementValues.forEach {
            val element = it.key
            val annotationValue = it.value

            when (element.simpleName.toString()) {
                "protocolNumber" -> {
                    protocolNumber = annotationValue.value as Int
                }
                "customSerialization" -> {
                    customSerialization = annotationValue.value as Boolean
                }
                "elementIndex" -> {

                    val elementTypeValueArray = annotationValue as Attribute.Array

                    val elementTypeValueDatas = try {
                        elementTypeValueArray.values.map {
                            parseElementIndex(processingEnv, it)
                        }
                    } catch (e: Exception) {
                        processingEnv.messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "parseElementTypeVaule:${e}\r\n"
                        )
                        throw e
                    }

                    elementValues = elementTypeValueDatas.toTypedArray()
                }
            }
        }

        return ProtocolAnnotationData(protocolNumber, customSerialization, elementValues!!)
    }

    fun parseElementIndex(processingEnv: ProcessingEnvironment, attribute: Attribute): ElementIndexData {

        val compound = attribute.value as Attribute.Compound
        //first step fetch elementType
        val elementTypeAttribute =
            compound.elementValues.entries.first { it.key.name.toString() == "elementType" }

//        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "elementType:${elementTypeAttribute}\r\n")
//        processingEnv.messager.printMessage(
//            Diagnostic.Kind.NOTE,
//            "elementTypeAttribute.value:${elementTypeAttribute.value}\r\n"
//        )
//
//        processingEnv.messager.printMessage(
//            Diagnostic.Kind.NOTE,
//            "elementTypeAttribute.value.value:${elementTypeAttribute.value.value}\r\n"
//        )

        val elementTypeEnum = elementTypeAttribute.value as Attribute.Enum
        val elementTypeName = elementTypeEnum.value.name.toString()

        val elementType = ElementType.valueOf(elementTypeName)

        val elementIndexAttribute = compound.elementValues.entries.first {
            it.key.name.toString() == "elementIndex"
        }

        val elementIndexValue = elementIndexAttribute.value.value as Int

        val elementValueAttribute = compound.elementValues.entries.first {
            it.key.name.toString() == "elementValue"
        }

        val elementValue = elementValueAttribute.value.value as String

        return ElementIndexData(elementIndexValue, elementType, elementValue)
    }


}

data class ElementIndexData(
    val elementIndex: Int,
    val elementType: ElementType,
    val elementValue: String
)

data class ProtocolAnnotationData(
    val protocolNumber: Int,
    val customSerialization: Boolean,
    val expectedElementIndex: Array<ElementIndexData>
) {
}