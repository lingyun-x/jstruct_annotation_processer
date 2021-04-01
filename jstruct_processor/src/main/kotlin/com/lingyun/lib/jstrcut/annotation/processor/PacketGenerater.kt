package com.lingyun.lib.jstrcut.annotation.processor

import com.lingyun.lib.jstruct.annotation.StructAnnotation
import com.lingyun.lib.jstruct.annotation.TypeMirrorUtils
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
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
@ExperimentalStdlibApi
object PacketGenerater {

    fun generatePacketImpl(
        element: Element,
        file: File,
        processingEnv: ProcessingEnvironment
    ) {

        processingEnv.messager.printMessage(
            Diagnostic.Kind.WARNING,
            "generatePacketImpl element :$element \r\n"
        )

        val classElement = element as TypeElement
        val typeString = classElement.asType().toString()
        val lastDotIndex = typeString.lastIndexOf(".")
        val packageName = typeString.substring(0, lastDotIndex)
        val className = typeString.substring(lastDotIndex + 1)

        val packetStructFunc = generatePacketStructFunc(element, processingEnv)
        val elementFuncs = elementFuncs(processingEnv, element, ArrayList(), true)
        val applyElementFuncs = fieldApplyElementFunc(processingEnv, classElement, "this", ArrayList<String>())

        val classNameImpl = className + "Impl"

        val fileSpecBuilder = FileSpec.builder(packageName, classNameImpl)
            .addType(
                TypeSpec.classBuilder(classNameImpl)
                    .superclass(ClassName(packageName, className))
                    .addFunction(packetStructFunc)
                    .addFunctions(elementFuncs)
                    .addFunctions(applyElementFuncs)
                    .build()
            )

        val fileSpec = fileSpecBuilder.build()
        fileSpec.writeTo(file)

        processingEnv.messager.printMessage(
            Diagnostic.Kind.WARNING,
            "generatePacketImpl element :$element finish\r\n"
        )
    }

    fun getElementStruct(element: Element, processingEnv: ProcessingEnvironment): String {
        val fieldStructSb = StringBuffer()
        when (element) {
            is TypeElement -> {
                val fieldElements = getFieldElements(processingEnv, element)

                for (e in fieldElements) {
                    fieldStructSb.append(getElementStruct(e, processingEnv))
                }
            }
            is VariableElement -> {
                val typeMirror = element.asType()
                val structAnnotation = element.getAnnotation(StructAnnotation::class.java)
                var fieldStruct = ""
                if (structAnnotation != null) {
                    fieldStruct = structAnnotation.struct
                }

                when {
                    TypeMirrorUtils.isByte(typeMirror) || TypeMirrorUtils.isByteBox(typeMirror) -> {
                        fieldStructSb.append("1b")
                    }
                    TypeMirrorUtils.isShort(typeMirror) || TypeMirrorUtils.isShortBox(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            fieldStructSb.append("1h")
                        }
                    }
                    TypeMirrorUtils.isInt(typeMirror) || TypeMirrorUtils.isIntBox(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            fieldStructSb.append("1i")
                        }
                    }
                    TypeMirrorUtils.isLong(typeMirror) || TypeMirrorUtils.isLongBox(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            fieldStructSb.append("1l")
                        }
                    }
                    TypeMirrorUtils.isFloat(typeMirror) || TypeMirrorUtils.isFloatBox(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            fieldStructSb.append("1f")
                        }
                    }
                    TypeMirrorUtils.isDouble(typeMirror) || TypeMirrorUtils.isDoubleBox(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            fieldStructSb.append("1d")
                        }
                    }
                    TypeMirrorUtils.isByteArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("ByteArray need struct")
                        }
                    }
                    TypeMirrorUtils.isCharArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("ByteArray need struct")
                        }
                    }
                    TypeMirrorUtils.isShortArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("ShortArray need struct")
                        }
                    }
                    TypeMirrorUtils.isIntArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("IntArray need struct")
                        }
                    }
                    TypeMirrorUtils.isLongArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("LongArray need struct")
                        }
                    }
                    TypeMirrorUtils.isFloatArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("FloatArray need struct")
                        }
                    }
                    TypeMirrorUtils.isDoubleArray(typeMirror) -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            throw IllegalAccessException("DoubleArray need struct")
                        }
                    }
                    typeMirror.kind == TypeKind.ARRAY -> {
                        if (fieldStruct.isNotEmpty()) {
                            //need to fill with item struct
                            if (fieldStruct.endsWith("[?]")) {
                                val arrayType = typeMirror as ArrayType

                                if (arrayType.componentType.kind == TypeKind.DECLARED) {
                                    val componentElement = (arrayType.componentType as DeclaredType).asElement()

                                    val componentStructType = getElementStruct(componentElement, processingEnv)
                                    val numberExpression = fieldStruct.substring(0, fieldStruct.length - 3)

                                    fieldStructSb.append(numberExpression)
                                    fieldStructSb.append("[")
                                    fieldStructSb.append(componentStructType)
                                    fieldStructSb.append("]")
                                }
                            } else {
                                fieldStructSb.append(fieldStruct)
                            }
                        } else {
                            throw IllegalAccessException("Array need struct")
                        }
                    }
                    typeMirror.kind == TypeKind.DECLARED -> {
                        if (fieldStruct.isNotEmpty()) {
                            fieldStructSb.append(fieldStruct)
                        } else {
                            val declaredType = typeMirror as DeclaredType
                            val fieldTypeElement = declaredType.asElement()

                            fieldStructSb.append("1{")
                            val fs = getElementStruct(fieldTypeElement, processingEnv)
                            fieldStructSb.append(fs)
                            fieldStructSb.append("}")
                        }
                    }
                    else -> {
                        throw IllegalAccessException("not support this type:${typeMirror.kind}")
                    }

                }

            }
        }

        return fieldStructSb.toString()
    }

    fun generatePacketStructFunc(element: TypeElement, processingEnv: ProcessingEnvironment): FunSpec {
        val bodyFuncBuilder = FunSpec.builder("packetStruct")
            .addModifiers(KModifier.OVERRIDE)
            .returns(String::class)

        val struct = getElementStruct(element, processingEnv)
        bodyFuncBuilder.addStatement("return %S", struct)
        return bodyFuncBuilder.build()
    }

    fun elementFuncName(element: Element, root: Boolean): String {
        val typeMirror = element.asType()
        val typeString = typeMirror.toString()
        val lastDotIndex = typeString.lastIndexOf(".")
        val packageName = typeString.substring(0, lastDotIndex)
        val className = typeString.substring(lastDotIndex + 1)

        val elementCla = classNameForElement(element)

        val funcName = if (root) "elements" else "elementsOf${className}"
        return funcName
    }

    fun elementFuncs(
        processingEnv: ProcessingEnvironment,
        element: TypeElement, funcNames: MutableList<String>,
        root: Boolean
    ): List<FunSpec> {
        val funcSpecs = ArrayList<FunSpec>()
        val listAnyType = List::class.parameterizedBy(Any::class)

        val elementName = element.simpleName.toString()
        val typeMirror = element.asType()
        val typeString = typeMirror.toString()
        val elementCla = classNameForElement(element)

        val funcName = elementFuncName(element, root)

        if (funcNames.contains(funcName)) {
            return funcSpecs
        }

        funcNames.add(funcName)

        val bodyFuncBuilder = FunSpec.builder(funcName)
            .returns(listAnyType)

        if (!root) {
            bodyFuncBuilder.addParameter("${StringUtil.replaceFirstCharToLow(elementName)}", elementCla)
        } else {
            bodyFuncBuilder.addModifiers(KModifier.OVERRIDE)
        }

        bodyFuncBuilder.addStatement("val elements = ArrayList<Any>()")
        bodyFuncBuilder.addStatement("var fieldArrayElements:%T", listAnyType)

        val fieldElements = getFieldElements(processingEnv, element)
        fieldElements.forEach {
            val fieldTypeMirror = it.asType()
            val fieldName = it.simpleName.toString()
            val fieldElementName = if (root) fieldName else "${StringUtil.replaceFirstCharToLow(elementName)}.$fieldName"

            when {
                fieldTypeMirror.kind.isPrimitive -> {
                    bodyFuncBuilder.addStatement("elements.add(%L)", fieldElementName)
                }
                fieldTypeMirror.kind == TypeKind.ARRAY -> {

                    val arrayType = fieldTypeMirror as ArrayType
                    val componentType = arrayType.componentType

                    when {
                        componentType.kind.isPrimitive -> {
                            bodyFuncBuilder.addStatement("elements.add(${fieldElementName})")
                        }
                        componentType.kind == TypeKind.DECLARED -> {
                            val componentTypeElement = (componentType as DeclaredType).asElement() as TypeElement
                            val fieldFuncName = elementFuncName(componentTypeElement, false)
                            val fieldFucs = elementFuncs(processingEnv, componentTypeElement, funcNames, false)
                            funcSpecs.addAll(fieldFucs)

                            bodyFuncBuilder.addStatement("fieldArrayElements = ArrayList()")
                            bodyFuncBuilder.beginControlFlow("for (i in %L until ${fieldElementName}.size)", 0)
                            bodyFuncBuilder.addStatement("fieldArrayElements.add($fieldFuncName($fieldElementName[i]))")
                            bodyFuncBuilder.endControlFlow()
                            bodyFuncBuilder.addStatement("elements.add(fieldArrayElements)")
                        }
                    }
                }
                fieldTypeMirror.kind == TypeKind.DECLARED -> {
                    val fieldFuncName = elementFuncName(it, false)
                    val fieldElement = (fieldTypeMirror as DeclaredType).asElement() as TypeElement
                    val fieldFucs = elementFuncs(processingEnv, fieldElement, funcNames, false)
                    funcSpecs.addAll(fieldFucs)
                    bodyFuncBuilder.addStatement("elements.add($fieldFuncName($fieldName))")
                }
            }

        }

        bodyFuncBuilder.addStatement("return elements")
        funcSpecs.add(bodyFuncBuilder.build())
        return funcSpecs
    }

    fun fieldApplyElementFunc(
        processingEnv: ProcessingEnvironment,
        fieldElement: TypeElement,
        fieldName: String,
        funcNames: MutableList<String>,
    ): List<FunSpec> {
        val funcSpecs = ArrayList<FunSpec>()
        val listAnyType = List::class.parameterizedBy(Any::class)

        val typeString = TypeMirrorUtils.getElementTypeClassString(fieldElement)

        val lastDotIndex = typeString.lastIndexOf(".")
        val packageName = typeString.substring(0, lastDotIndex)
        val className = typeString.substring(lastDotIndex + 1)

        val fieldCla = ClassName(packageName, className)

        val funcName =
            if (fieldName == "this") "applyElements" else "applyElementsOf${StringUtil.replaceFirstCharToUp(fieldName)}"

        if (funcNames.contains(funcName)) {
            return funcSpecs
        }

        funcNames.add(funcName)


        val bodyFuncBuilder = FunSpec.builder(funcName)
            .addParameter("elements", listAnyType)
            .returns(Int::class)

        if (fieldName != "this") {
            bodyFuncBuilder.addParameter(fieldName, fieldCla)
        } else {
            bodyFuncBuilder.addModifiers(KModifier.OVERRIDE)
        }

        bodyFuncBuilder.addStatement("var fieldArrayElements:%T", listAnyType)

        val filedFieldElements = getFieldElements(processingEnv, fieldElement)
        var elementIndex = 0

        filedFieldElements.forEach {
            val typeMirror = it.asType()
            val elementName = it.simpleName.toString()
            val fieldElementName = "$fieldName.$elementName"

            when {
                typeMirror.kind.isPrimitive -> {
                    val fieldTypeString = TypeMirrorUtils.getElementTypeClassString(it)
                    bodyFuncBuilder.addStatement(
                        "${fieldName}.${elementName} = elements.get(${elementIndex++}) as ${fieldTypeString}",
                    )
                }
                typeMirror.kind == TypeKind.ARRAY -> {
                    val arrayType = typeMirror as ArrayType
                    val componentType = arrayType.componentType

                    when {
                        componentType.kind.isPrimitive -> {
                            val fieldTypeString = TypeMirrorUtils.getElementTypeClassString(it)
                            bodyFuncBuilder.addStatement(
                                "${fieldName}.${elementName} = elements.get(${elementIndex++}) as ${fieldTypeString}",
                            )
                        }
                        componentType.kind == TypeKind.DECLARED -> {
                            val arrayFieldTypeElement = (componentType as DeclaredType).asElement() as TypeElement
                            val arrayFieldSimpleName = arrayFieldTypeElement.simpleName.toString()

                            val arrayFieldClass = classNameForElement(arrayFieldTypeElement)

                            bodyFuncBuilder.addStatement(
                                "fieldArrayElements = elements.get(${elementIndex++}) as %T",
                                listAnyType
                            )

                            bodyFuncBuilder.addStatement(
                                "$elementName = %T(fieldArrayElements.size){ %T() }",
                                Array::class, arrayFieldClass
                            )

                            val declaredFuncs =
                                fieldApplyElementFunc(
                                    processingEnv,
                                    arrayFieldTypeElement,
                                    StringUtil.replaceFirstCharToLow(arrayFieldSimpleName),
                                    funcNames
                                )
                            funcSpecs.addAll(declaredFuncs)

                            bodyFuncBuilder.beginControlFlow("for(i in 0 until fieldArrayElements.size)")
                            bodyFuncBuilder.addStatement(
                                "applyElementsOf${arrayFieldSimpleName}(fieldArrayElements[i] as %T,%L[i])",
                                listAnyType, fieldElementName
                            )
                            bodyFuncBuilder.endControlFlow()
                        }
                    }
                }
                typeMirror.kind == TypeKind.DECLARED -> {
                    val declaredTypeElement = (typeMirror as DeclaredType).asElement() as TypeElement
                    val declaredSimpleName = declaredTypeElement.simpleName.toString()
                    val declaredFuncs =
                        fieldApplyElementFunc(
                            processingEnv,
                            declaredTypeElement,
                            StringUtil.replaceFirstCharToLow(declaredSimpleName),
                            funcNames
                        )
                    funcSpecs.addAll(declaredFuncs)
                    bodyFuncBuilder.addStatement(
                        "applyElementsOf${declaredSimpleName}(elements[${elementIndex++}] as %T,%L)",
                        listAnyType, elementName
                    )
                }

            }
        }

        bodyFuncBuilder.addStatement("return ${elementIndex}")
        funcSpecs.add(bodyFuncBuilder.build())
        return funcSpecs
    }

    fun getFieldElements(
        processingEnv: ProcessingEnvironment,
        element: Element
    ): List<Element> {
        val fieldElements = try {
            element.enclosedElements
                .filter { it is VariableElement }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "invoke fields fail:${e} \r\n"
            )
            throw e
        }
        return fieldElements
    }

    private fun getSuperclass(
        processingEnv: ProcessingEnvironment,
        type: TypeElement
    ): TypeElement? {
        return if (type.superclass.kind == TypeKind.DECLARED) {
            val superclass = processingEnv.getTypeUtils().asElement(type.superclass) as TypeElement
//            if (superclass.kind.isInterface){
//                return null
//            }
            val name = superclass.qualifiedName.toString()
            if (name.startsWith("java.")
                || name.startsWith("javax.")
                || name.startsWith("android.")
            ) {
                // Skip system classes, this just degrades performance
                null
            } else {
                superclass
            }
        } else {
            null
        }
    }

    fun classNameForElement(element: Element): ClassName {
        val typeMirror = element.asType()
        val typeString = typeMirror.toString()
        val lastDotIndex = typeString.lastIndexOf(".")
        val packageName = typeString.substring(0, lastDotIndex)
        val className = typeString.substring(lastDotIndex + 1)

        val cla = ClassName(packageName, className)
        return cla
    }

}