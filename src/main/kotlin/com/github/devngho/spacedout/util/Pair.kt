package com.github.devngho.spacedout.util

import kotlin.Pair

class Pair<A, B>(val first: A,
                 val second: B) {
    fun toKotlinPair(): Pair<A, B>{
        return Pair(first, second)
    }
}