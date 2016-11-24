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
    fun modifyOption(ƒ: (A) -> A): (S) -> S? = { s -> getOption(s)?.let { a -> reverseGet(ƒ(a)) } }
    infix fun <B> compose(other: Prism<A, B>): Prism<S, B> = Prism(
            getOption = { s -> getOption(s)?.let { a -> other.getOption(a) } },
            reverseGet = { b -> reverseGet(other.reverseGet(b)) }
    )
}

object Kotlens {
    fun <S, A> Iso<S, A>.toPrism(): Prism<S, A> = Prism(getOption = get, reverseGet = reverseGet)

    infix fun <S ,A, B> Iso<S, A>.compose(other: Prism<A, B>): Prism<S, B> = toPrism() compose other
    infix fun <S, A, B> Prism<S, A>.compose(other: Iso<A, B>): Prism<S, B> = this compose other.toPrism()
}

fun Any.todo(): Nothing = throw NotImplementedError(this.toString())

class Try<out T>(val ƒ: () -> T) {
    fun toOption(): T? = try { ƒ() } catch (t: Throwable) { null }
}