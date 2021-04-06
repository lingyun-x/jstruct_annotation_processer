package com.lingyun.lib.jstrcut.annotation.processor

import com.lingyun.lib.jstruct.annotation.Embed
import com.lingyun.lib.jstruct.annotation.Ignore
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

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