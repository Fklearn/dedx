package com.dedx.dex.struct

import com.android.dx.rop.code.AccessFlags

interface AccessInfo {
    val accFlags: Int
    fun isPublic(): Boolean {
        return (accFlags and AccessFlags.ACC_PUBLIC) != 0
    }
    fun isProtected(): Boolean {
        return (accFlags and AccessFlags.ACC_PROTECTED) != 0
    }
    fun isFinal(): Boolean {
        return (accFlags and AccessFlags.ACC_FINAL) != 0
    }
}