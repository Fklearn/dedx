package com.dedx.transform

import com.dedx.dex.struct.ClassNode
import com.dedx.dex.struct.MethodNode
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.V1_8
import java.io.File
import java.io.FileOutputStream

class ClassTransformer(val clsNode: ClassNode, val filePath: String = ""): Opcodes {
    val classWriter = ClassWriter(0)
    var fieldVisitor: FieldVisitor? = null

    fun visitClass(): ClassTransformer {
        classWriter.visit(V1_8,
                clsNode.accFlags,
                clsNode.clsInfo.fullName.replace('.', '/'),
                null,
                if (clsNode.hasSuperClass()) clsNode.superClassNameWithSlash() else "java/lang/Object",
                null/*TODO interfaces*/)

        // TODO: set source file
//        classWriter.visitSource()

        var main: MethodNode? = null
        for (mthNode in clsNode.methods) {
            if (mthNode.isMain()) {
                main = mthNode
                continue
            }
            MethodTransformer(mthNode, this).visitMethod()
        }
        if (main != null) MethodTransformer(main, this).visitMethod()
        classWriter.visitEnd()
        return this
    }

    fun dump(): String {
            FileOutputStream(File(filePath)).use {
                w -> w.write(classWriter.toByteArray())
            }
            return filePath
    }

    fun toFile(path: String) = FileOutputStream(File(path)).write(classWriter.toByteArray())
}