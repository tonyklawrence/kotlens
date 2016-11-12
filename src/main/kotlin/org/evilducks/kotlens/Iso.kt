package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(m: (A) -> A): (S) -> S = todo()
    fun reverse(): Iso<A, S> = Iso(reverseGet, get)
    fun <B> combine(other: Iso<A, B>): Iso<S, B> = todo()
}