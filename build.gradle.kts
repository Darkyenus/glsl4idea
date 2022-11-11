import org.jetbrains.kotlin.js.common.isValidES5Identifier
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.FileFilter
import java.util.regex.Pattern
import java.time.Instant

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.9.0"
}

val pluginVersion = "1.24-SNAPSHOT"

group = "com.darkyen"
version = pluginVersion

// Configure project's dependencies
repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    type.set("IC")
    version.set("2022.2.3")
    //version.set("IC-212.4746.92")
    updateSinceUntilBuild.set(false)
    instrumentCode.set(false)

    //plugins.add("PsiViewer:222-SNAPSHOT")
}

tasks {
    // Set the JVM compatibility versions
    // Java language level used to compile sources and to generate the files for - Java 11 is required since 2020.3
    val javaVersion = "17"
    withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = javaVersion
    }

    patchPluginXml {
        version.set(pluginVersion)
    }

    // Because it is broken and crashes the build
    buildSearchableOptions.get().enabled = false

    runIde.configure {
        jvmArgs!!.add("-Didea.ProcessCanceledException=disabled")
    }
}

tasks.register("generateGLSLHeaders", Exec::class.java) {
    val workDir = File(rootDir, "build/generate_glsl_headers").apply {
        mkdirs()
    }
    val refDir = File(workDir, "ref")

    val docUrl = "https://github.com/KhronosGroup/OpenGL-Refpages"

    if (refDir.exists()) {
        workingDir = refDir
        commandLine = listOf("git", "pull")
    } else {
        workingDir = workDir
        commandLine = listOf("git", "clone", "--depth=1", docUrl+".git", refDir.name)
    }

    outputs.upToDateWhen { false }

    doLast {
        val glslDocFiles = File(refDir, "gl4").listFiles(object : FileFilter {
            val nameRegex = Pattern.compile("")
            val ignoreFiles = setOf("removedTypes.xml",// Does not contain anything, but uses misleading xml root
                // All part of gl_PerVertex with non-consistent xml structure
                "gl_PointSize.xml", "gl_Position.xml", "gl_ClipDistance.xml", "gl_CullDistance.xml"
            )
            override fun accept(file: File?): Boolean {
                val name = file?.name ?: ""
                if (file != null && file.isFile && name.endsWith(".xml", ignoreCase = true) && name !in ignoreFiles) {
                    if (name.startsWith("gl") && name.length >= 3 && name[2].isUpperCase()) {
                        // These are all OpenGL functions, not GLSL
                        // Uppercase checks for gl_VertexID-like GLSL variables
                        return false
                    }
                    return true
                }
                return false
            }
        })

        glslDocFiles.sortBy { it.name }

        data class TypeName constructor(
            /** Type of the function/parameter/variable */
            val type: String,
            /** -1 = not an array, 0 = unsized array, >0 = array of this size */
            val array:Int,
            /** name of the function/parameter/variable */
            val name: String,
            /** Modifier, if any */
            val modifier: String?,
            /** Whether the argument is optional (only possible on last parameters) */
            val optional: Boolean) {
            fun combine(other: TypeName): TypeName {
                if (type != other.type || array != other.array || name != other.name || optional != other.optional) {
                    throw AssertionError("Can't combine $this with $other")
                }
                if (this.modifier != null && other.modifier == null) {
                    return this
                }
                if (this.modifier == null && other.modifier != null) {
                    return other
                }
                if (this.modifier == other.modifier) {
                    return this
                }
                throw AssertionError("Can't combine $this with $other")
            }
        }
        val validModifiers = setOf("in", "out", "inout", "const")

        fun literalType(type: String): Pair<String, List<String>> {
            return type to listOf(type)
        }
        fun genericVecSize(base: String): Pair<String, List<String>> {
            return base to ArrayList<String>().apply {
                for (d in 2..4) {
                    add(base+d)
                }
            }
        }
        fun genericMatSize(base: String): Pair<String, List<String>> {
            return base to ArrayList<String>().apply {
                for (a in 2..4) {
                    for (b in 2..4) {
                        if (a == b) {
                            add(base+a)
                        } else {
                            add(base+a+"x"+b)
                        }
                    }
                }
            }
        }

        /**
         * When key type is encountered, it means the type(s) on the right
         */
        // https://github.com/KhronosGroup/OpenGL-Registry/blob/main/specs/gl/GLSLangSpec.4.60.html
        val validTypesAndAliases = HashMap<String, List<String>>().apply {
            put("void", listOf("void"))
            put("atomic_uint", listOf("atomic_uint"))

            for ((prefix, baseType) in listOf("" to "float", "d" to "double", "b" to "bool", "i" to "int", "u" to "uint")) {
                // gen type
                val genType = ArrayList<String>()// Primitive and vectors
                put("gen"+prefix.toUpperCase()+"Type", genType)

                // Primitive
                put(baseType, listOf(baseType))
                genType.add(baseType)

                // Vector
                val vecType = ArrayList<String>()
                put(prefix+"vec", vecType)
                for (i in 2..4) {
                    val vType = prefix+"vec"+i
                    genType.add(vType)
                    vecType.add(vType)
                    put(vType, listOf(vType))
                }

                // Matrix
                val matType = ArrayList<String>()
                put(prefix+"mat", matType)
                for (a in 2..4) {
                    for (b in 2..4) {
                        val full = prefix+"mat"+a+"x"+b
                        val canon = if (a == b) prefix+"mat"+a else full
                        if (full !== canon) {
                            put(full, listOf(canon))
                        }
                        matType.add(canon)
                        put(canon, listOf(canon))
                    }
                }
            }

            put("gvec4", listOf("vec4", "ivec4", "uvec4", "dvec4"/* not mentioned in 4.60 spec, but maybe it is an extension?*/))

            val handles = listOf(
                "sampler1D",
                "texture1D",
                "image1D",
                "sampler1DShadow",
                "sampler1DArray",
                "texture1DArray",
                "image1DArray",
                "sampler1DArrayShadow",
                "sampler2D",
                "texture2D",
                "image2D",
                "sampler2DShadow",
                "sampler2DArray",
                "texture2DArray",
                "image2DArray",
                "sampler2DArrayShadow",
                "sampler2DMS",
                "texture2DMS",
                "image2DMS",
                "sampler2DMSArray",
                "texture2DMSArray",
                "image2DMSArray",
                "sampler2DRect",
                "texture2DRect",
                "image2DRect",
                "sampler2DRectShadow",
                "sampler3D",
                "texture3D",
                "image3D",
                "samplerCube",
                "textureCube",
                "imageCube",
                "samplerCubeShadow",
                "samplerCubeArray",
                "textureCubeArray",
                "imageCubeArray",
                "samplerCubeArrayShadow",
                "samplerBuffer",
                "textureBuffer",
                "imageBuffer",
                "subpassInput",
                "subpassInputMS",
                "isampler1D",
                "itexture1D",
                "iimage1D",
                "isampler1DArray",
                "itexture1DArray",
                "iimage1DArray",
                "isampler2D",
                "itexture2D",
                "iimage2D",
                "isampler2DArray",
                "itexture2DArray",
                "iimage2DArray",
                "isampler2DMS",
                "itexture2DMS",
                "iimage2DMS",
                "isampler2DMSArray",
                "itexture2DMSArray",
                "iimage2DMSArray",
                "isampler2DRect",
                "itexture2DRect",
                "iimage2DRect",
                "isampler3D",
                "itexture3D",
                "iimage3D",
                "isamplerCube",
                "itextureCube",
                "iimageCube",
                "isamplerCubeArray",
                "itextureCubeArray",
                "iimageCubeArray",
                "isamplerBuffer",
                "itextureBuffer",
                "iimageBuffer",
                "isubpassInput",
                "isubpassInputMS",
                "usampler1D",
                "utexture1D",
                "uimage1D",
                "usampler1DArray",
                "utexture1DArray",
                "uimage1DArray",
                "usampler2D",
                "utexture2D",
                "uimage2D",
                "usampler2DArray",
                "utexture1DArray",
                "uimage2DArray",
                "usampler2DMS",
                "utexture2DMS",
                "uimage2DMS",
                "usampler2DMSArray",
                "utexture2DMSArray",
                "uimage2DMSArray",
                "usampler2DRect",
                "utexture2DRect",
                "uimage2DRect",
                "usampler3D",
                "utexture3D",
                "uimage3D",
                "usamplerCube",
                "utextureCube",
                "uimageCube",
                "usamplerCubeArray",
                "utextureCubeArray",
                "uimageCubeArray",
                "usamplerBuffer",
                "utextureBuffer",
                "uimageBuffer",
                "usubpassInput",
                "usubpassInputMS",
                "sampler",
                "samplerShadow",
            )
            for (handle in handles) {
                put(handle, listOf(handle))
            }

            // Only those found in docs
            val genericHandles = listOf(
                "gimage1D",
                "gimage1DArray",
                "gimage2D",
                "gimage2DArray",
                "gimage2DMS",
                "gimage2DMSArray",
                "gimage2DRect",
                "gimage3D",
                "gimageBuffer",
                "gimageCube",
                "gimageCubeArray",
                "gsampler1D",
                "gsampler1DArray",
                "gsampler2D",
                "gsampler2DArray",
                "gsampler2DMS",
                "gsampler2DMSArray",
                "gsampler2DRect",
                "gsampler3D",
                "gsamplerBuffer",
                "gsamplerCube",
                "gsamplerCubeArray",
            )
            for (genericHandle in genericHandles) {
                if (genericHandle[0] != 'g') throw AssertionError(genericHandle+" is not g prefixed")
                val suffix = genericHandle.substring(1)
                val concreteHandles = handles.filter { it.length in suffix.length..suffix.length + 1 && it.endsWith(suffix) }
                //println("$genericHandle -> $concreteHandles")
                if (concreteHandles.size <= 1) throw AssertionError("$genericHandle has no concrete implementations")
                put(genericHandle, concreteHandles)
            }
            // I believe this one is a typo in the docs, because there is no mention in the standard of a type which contains "bufferImage" in it
            put("gbufferImage", get("gimageBuffer")!!)
        }
        /** These types appear in the docs, but don't actually exist. */
        val invalidTypes = setOf(
            "gimageRect"// https://github.com/KhronosGroup/GLSL/issues/173
        )

        val TypeNameSplitPattern = Pattern.compile("\\s+")
        val keywords = setOf("sample")// Just the problematic ones
        fun typeNameFrom(element: Element): TypeName? {
            var text = element.text().trim()
            if (text == "void") {
                return null
            }
            var optional = false
            if (text.startsWith("[") && text.endsWith("]")) {
                optional = true
                text = text.substring(1, text.length - 1)
            }
            var array = -1
            if (text.endsWith("]")) {
                val begin = text.indexOf('[')
                val arraySize = text.substring(begin + 1, text.length - 1)
                array = if (arraySize.isBlank()) 0 else arraySize.toInt()
                text = text.substring(0, begin)
            }

            val parts = text.split(TypeNameSplitPattern)
            if (parts.size !in 2..3 || parts.any { !it.isValidES5Identifier() }) {
                throw IllegalArgumentException("Invalid type/name:\n"+element.parent()?.outerHtml())
            }
            var modifier = if (parts.size == 3) parts.firstOrNull() else null
            if (modifier != null && modifier !in validModifiers) {
                throw IllegalArgumentException("Invalid modifier:\n"+ element.parent()?.outerHtml())
            }
            if (modifier == "in") {
                // in is default
                modifier = null
            }

            var name = parts[parts.size - 1]
            if (name in keywords) {
                name = "${name}_"
            }

            return TypeName(parts[parts.size - 2], array, name, modifier, optional)
        }

        // First TypeName belongs to the function itself, rest are parameters
        val functionDefinitions = ArrayList<List<TypeName>>()
        val fieldDefinitions = LinkedHashMap<String, TypeName>()

        for (glslDocFile in glslDocFiles) {
            val file = Jsoup.parse(glslDocFile, null, glslDocFile.absolutePath, Parser.xmlParser())
            val root = file.root().firstElementChild() ?: continue
            val rootTag = root.tagName()
            if (rootTag != "refentry") {
                continue
            }

            var gotFuncOrField = false
            root.select("funcprototype").forEach { prototype ->
                gotFuncOrField = true

                val funcDef: Element = prototype.firstElementChild()!!
                if (funcDef.tagName() != "funcdef") throw AssertionError("Not funcdef: "+prototype.outerHtml())
                val definition = ArrayList<TypeName>()
                val funcNameAndType = typeNameFrom(funcDef)!!
                definition.add(funcNameAndType)
                if (funcNameAndType.array != -1) throw AssertionError("No return arrays")
                if (funcNameAndType.modifier != null) throw AssertionError("No func modifiers")

                var sibling = funcDef.nextElementSibling()
                while (sibling != null) {
                    if (sibling.tagName() != "paramdef") throw AssertionError("Not paramdef: "+prototype.outerHtml())
                    val paramTypeName = typeNameFrom(sibling)
                    if (paramTypeName == null) {
                        if (definition.size != 1 || sibling.nextElementSibling() != null) {
                            // void must be alone in parameter list
                            throw IllegalArgumentException(funcDef.outerHtml())
                        }
                        break
                    } else {
                        definition.add(paramTypeName)
                        sibling = sibling.nextElementSibling()
                    }
                }

                for (i in 0 until definition.size - 1) {
                    if (definition[i].optional) {
                        throw IllegalArgumentException("Only last argument can be optional: $definition")
                    }
                }

                if (definition.any { it.type in invalidTypes }) {
                    // Bogus definition
                    return@forEach
                }

                for (name in definition) {
                    if (name.type !in validTypesAndAliases) {
                        throw AssertionError("Unknown type "+name.type+" of "+prototype.outerHtml())
                    }
                }

                functionDefinitions.add(definition)
            }

            root.select("fieldsynopsis").forEach { fieldSynopsis ->
                gotFuncOrField = true
                val fieldDef = typeNameFrom(fieldSynopsis)!!
                if (validTypesAndAliases[fieldDef.type]!!.size != 1) {
                    throw AssertionError("Field definitions can't have generic types: "+fieldSynopsis.outerHtml())
                }
                if (fieldDef.optional) {
                    throw AssertionError("Field definitions can't be optional: "+fieldSynopsis.outerHtml())
                }
                val previous = fieldDefinitions.put(fieldDef.name, fieldDef)
                if (previous != null) {
                    val combined = previous.combine(fieldDef)
                    fieldDefinitions[combined.name] = combined
                }
            }

            if (!gotFuncOrField) {
                println("Nothing actionable found in "+glslDocFile.name)
            }

        }

        val resultGLSL = buildString {
            append("// GLSL Built-ins\n")
            append("// Generated on ").append(Instant.now()).append("\n")
            append("// Generated from documentation at ").append(docUrl).append("\n")

            // Documentation of these is not 100% consistent, so just do it manually
            append("""

out gl_PerVertex {
    vec4 gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    float gl_CullDistance[];
};

            """.trimIndent()).append("\n")

            for (fieldDefinition in fieldDefinitions.values) {
                if (fieldDefinition.modifier != null) {
                    append(fieldDefinition.modifier).append(" ")
                }
                append(fieldDefinition.type).append(" ")
                append(fieldDefinition.name)
                if (fieldDefinition.array == 0) {
                    append("[]")
                } else if (fieldDefinition.array > 0) {
                    append('[').append(fieldDefinition.array).append(']')
                }
                append(";\n\n")
            }


            fun resolveFunctionGenerics(functionDefinition: List<TypeName>): Iterator<List<TypeName>> {
                return iterator {
                    val result = ArrayList<TypeName>()
                    val resolutions = HashMap<String, String>()
                    suspend fun SequenceScope<List<TypeName>>.resolvePart(i: Int) {
                        if (i == functionDefinition.size) {
                            yield(result)
                            if (result.last().optional) {
                                val withoutLast = result.removeLast()
                                yield(result)
                                result.add(withoutLast)
                            }
                            return
                        }
                        val part = functionDefinition[i]
                        val alreadyResolved = resolutions[part.type]
                        if (alreadyResolved != null) {
                            result.add(part.copy(type = alreadyResolved))
                            resolvePart(i + 1)
                            result.removeLast()
                        } else {
                            for (variant in validTypesAndAliases[part.type]!!) {
                                resolutions[part.type] = variant
                                result.add(part.copy(type = variant))
                                resolvePart(i + 1)
                                result.removeLast()
                            }
                            resolutions.remove(part.type)
                        }
                    }
                    resolvePart(0)
                }
            }

            var lastFunctionName = ""
            for (functionDefinitionRaw in functionDefinitions) {
                if (lastFunctionName != functionDefinitionRaw[0].name) {
                    append('\n')
                }
                lastFunctionName = functionDefinitionRaw[0].name

                for (functionDefinition in resolveFunctionGenerics(functionDefinitionRaw)) {
                    val base = functionDefinition[0]
                    append(base.type).append(' ').append(base.name).append('(')

                    for (p in 1 until functionDefinition.size) {
                        if (p != 1) append(", ")
                        val param = functionDefinition[p]
                        if (param.modifier != null) {
                            append(param.modifier).append(' ')
                        }

                        append(param.type).append(' ').append(param.name)
                        if (param.array == 0) {
                            append("[]")
                        } else if (param.array > 0) {
                            append('[').append(param.array).append(']')
                        }
                    }
                    append(");\n")
                }
            }
        }

        println(resultGLSL)

        val outFile = File(rootDir, "src/main/resources/glsl/builtin.glsl")
        outFile.parentFile.mkdirs()
        outFile.writeText(resultGLSL)
    }
}