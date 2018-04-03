package kotlens

import funk.compose

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(f: (A) -> A): (S) -> S = { s -> reverseGet(f(get(s))) }
    fun reverse(): Iso<A, S> = Iso(reverseGet, get)

    infix fun <B> compose(other: Iso<A, B>): Iso<S, B> = Iso(
            get = other.get compose get,
            reverseGet = reverseGet compose other.reverseGet
    )
}