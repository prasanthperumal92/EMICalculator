package com.prasanth.emicalc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform