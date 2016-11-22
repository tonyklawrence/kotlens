package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(f: (A) -> A): (S) -> S = { s -> reverseGet(f(get(s))) }
    fun reverse(): Iso<A, S> = Iso(reverseGet, get)
    infix fun <B> compose(other: Iso<A, B>): Iso<S, B> = Iso(
            get = { s -> other.get(get(s)) },
            reverseGet = { b -> reverseGet(other.reverseGet(b)) }
    )
}

class Prism<S, A>(val getOption: (S) -> A?, val reverseGet: (A) -> S) {
    fun isMatching(s: S): Boolean = getOption(s) != null
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> modifyOption(ƒ)(s) ?: s }
    fun modifyOption(ƒ: (A) -> A): (S) -> S? = { s -> getOption(s)?.let { a -> reverseGet(ƒ(a)) }}
    infix fun <B> compose(other: Prism<A, B>): Prism<S, B> = todo()
    infix fun <B> compose(other: Iso<A, B>): Prism<S, B> = todo() // via extension?
}

fun Any.todo(): Nothing = throw NotImplementedError(this.toString())

class Try<out T>(val ƒ: () -> T) {
    fun toOption(): T? {
        try { return ƒ() }
        catch (t: Throwable) { return null }
    }
}