package com.lingyun.lib.jstrcut.annotation.processor


import com.google.auto.service.AutoService
import com.lingyun.lib.jstruct.annotation.ElementType
import com.lingyun.lib.jstruct.annotation.Ignore
import com.lingyun.lib.jstruct.annotation.ProtocolAnnotation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
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
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(ProtocolProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ProtocolProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            ProtocolAnnotation::class.java.canonicalName
        )
    }

    @ExperimentalStdlibApi
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.NOTE,
            "begin process -------------------------------------------- \r\n"
        )

        try {
            return _process(annotations, roundEnv)
        } catch (e: Exception) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "process error:$e")
            throw e
        }
    }

    @ExperimentalStdlibApi
    private fun _process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val protocolInfos = ArrayList<ProtocolInfo>()

        val annotatedElements = roundEnv.getElementsAnnotatedWith(ProtocolAnnotation::class.java)

        annotatedElements.forEach {
            if (it.kind != ElementKind.CLASS) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Can only be applied to class,  element: $it \r\n"
                )
                throw IllegalArgumentException("not support this type:$it")
            }
            val ignoreElement: Element = processingEnv.elementUtils.getTypeElement(
                Ignore::class.java.name
            )
            val ignoreType = ignoreElement.asType()
            if (it.hasAnnotation(ignoreType)) {
                return@forEach
            }


            val packageElement = processingEnv.getElementUtils().getPackageOf(it)
            val packageName = packageElement.toString()
            val classElement = it as TypeElement

            val typeString = classElement.asType().toString()
            val className = typeString.substring(packageName.length + 1)
            val annotationData = ProtocolAnnotationMirrorUtils.parse(classElement, processingEnv)

            val protocolInfo = ProtocolInfo(it, packageName, className, annotationData)
            protocolInfos.add(protocolInfo)
        }

        if (protocolInfos.isEmpty()) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "protocolInfos is empty \r\n"
            )
            return true
        }

        try {
            generateProtocolMatcher(protocolInfos)
        } catch (e: Exception) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "generateProtocolMatcher error:${e} \r\n"
            )
            throw e
        }

        return true
    }

    @ExperimentalStdlibApi
    fun generateProtocolMatcher(protocolInfos: List<ProtocolInfo>) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.NOTE,
            "generateProtocol  ----- \r\n"
        )
        val generatedSourcesRoot: String =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()
        if (generatedSourcesRoot.isEmpty()) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "Can't find the target directory for generated Kotlin files.\r\n"
            )
            return
        }

        val file = File(generatedSourcesRoot)
        file.mkdir()

        val packetMatchCls = ClassName(
            "com.lingyun.lib.jstruct.protocol",
            "JStrcutProtocol"
        )

        val bodyFuncBuilder = FunSpec.builder("initProtocols")
        val packetIndexCla = ClassName("com.lingyun.lib.jstruct.protocol", "PacketIndex")
        bodyFuncBuilder.addStatement("var packetIndex:%T", packetIndexCla)

        for (info in protocolInfos) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "generateProtocol ${info.packetName} ${info.className}\r\n"
            )
            var cls = ClassName(info.packetName, info.className)

            //如果不是自定义序列化 则生成序列化类
            if (!info.protocolAnnotation.customSerialization) {
                PacketGenerater.generatePacketImpl(info.element, file, processingEnv)
                val className = info.className + "Impl"
                cls = ClassName(info.packetName, className)
            }

            val annotation = info.protocolAnnotation
            val protocolNumber = annotation.protocolNumber
            val exceptFirstElement = annotation.expectedElementIndex[0]
            val elementTypeCla = ClassName("com.lingyun.lib.jstruct.annotation", "ElementType")

            bodyFuncBuilder.addStatement(
                "packetIndex = %T(${exceptFirstElement.byteIndex},%T.%L,%S,null)",
                packetIndexCla, elementTypeCla, exceptFirstElement.elementType, exceptFirstElement.elementValue
            )

            for (i in 1 until annotation.expectedElementIndex.size) {
                bodyFuncBuilder.addStatement(
                    "packetIndex += %T(${annotation.expectedElementIndex[i].byteIndex},%T.%L,%S,null)",
                    packetIndexCla,
                    elementTypeCla,
                    annotation.expectedElementIndex[i].elementType,
                    annotation.expectedElementIndex[i].elementValue
                )
            }

            bodyFuncBuilder.addStatement(
                "%T.addPacketIndex(${protocolNumber},%T::class.java,packetIndex)",
                packetMatchCls,cls
            )
        }

        val fileSpecBuilder = FileSpec.builder(
            "com.eav.gs.online.lib.protocol",
            "PacketProtocols"
        )
            .addType(
                TypeSpec.objectBuilder("PacketProtocols")
                    .addFunction(
                        bodyFuncBuilder
                            .build()
                    )
                    .build()
            )

        val fileSpec = fileSpecBuilder.build()
        fileSpec.writeTo(file)
    }

}

data class ProtocolInfo(
    val element: Element,
    val packetName: String,
    val className: String,
    val protocolAnnotation: ProtocolAnnotationData
)