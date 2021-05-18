package com.lingyun.lib.jstrcut.annotation.processor

import com.lingyun.lib.jstruct.annotation.Embed
import com.lingyun.lib.jstruct.annotation.Ignore
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

/*
* Created by mc_luo on 2021/4/2 .
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

fun Element.hasAnnotation(annotationTypeMirror: TypeMirror): Boolean {
    return annotationMirrors.firstOrNull { it.annotationType == annotationTypeMirror } != null
}

fun Element.hasEmbedAnnotation(processingEnv: ProcessingEnvironment): Boolean {
    val embenElement: Element = processingEnv.elementUtils.getTypeElement(
        Embed::class.java.name
    )
    val embenType = embenElement.asType()
    return hasAnnotation(embenType)
}

fun getElementStructFields(
    processingEnv: ProcessingEnvironment,
    type: TypeElement,
    ves: MutableList<VariableElement>
) {

    val superTypeElement = com.lingyun.lib.jstrcut.annotation.processor.getSuperclass(processingEnv, type)

    if (superTypeElement != null) {
        com.lingyun.lib.jstrcut.annotation.processor.getElementStructFields(processingEnv, superTypeElement, ves)
    }

    val ignoreElement: Element = processingEnv.elementUtils.getTypeElement(
        Ignore::class.java.name
    )
    val ignoreType = ignoreElement.asType()

    val fieldElements = try {
        type.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .filter { it is VariableElement }
            .filter {
                val annotationMirrors = it.annotationMirrors
                annotationMirrors.firstOrNull { it.annotationType == ignoreType } == null
            }
    } catch (e: Exception) {
        processingEnv.messager.printMessage(
            Diagnostic.Kind.NOTE,
            "invoke fields fail:${e} \r\n"
        )
        throw e
    }

    ves.addAll(fieldElements.map { it as VariableElement })
}

private fun getSuperclass(
    processingEnv: ProcessingEnvironment,
    type: TypeElement
): TypeElement? {
    return if (type.superclass.kind == TypeKind.DECLARED) {
        val superclass = processingEnv.getTypeUtils().asElement(type.superclass) as TypeElement
        //skip interface
        if (superclass.kind.isInterface) {
            return null
        }
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
