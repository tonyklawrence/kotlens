package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(f: (A) -> A): (S) -> S = { s -> reverseGet(f(get(s))) }
    fun reverse(): Iso<A, S> = Iso(reverseGet, get)
    fun <B> combine(other: Iso<A, B>): Iso<S, B> = Iso(
            get = { s -> other.get(get(s)) },
            reverseGet = { b -> reverseGet(other.reverseGet(b)) }
    )
}