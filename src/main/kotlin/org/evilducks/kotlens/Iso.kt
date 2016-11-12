package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(m: (A) -> A): (S) -> S = notImplemented()
    fun reverse(): Iso<A, S> = notImplemented()
    fun <B> combine(other: Iso<A, B>): Iso<S, B> = notImplemented()
}

fun Any.notImplemented(): Nothing = throw NotImplementedError(this.toString())