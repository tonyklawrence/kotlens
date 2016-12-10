package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S) {
    fun modify(f: (A) -> A): (S) -> S = { s -> reverseGet(f(get(s))) }
    fun reverse(): Iso<A, S> = Iso(reverseGet, get)
    infix fun <B> compose(other: Iso<A, B>): Iso<S, B> = Iso(
            get = { s -> other.get(get(s)) },
            reverseGet = { b -> reverseGet(other.reverseGet(b)) }
    )
}

data class Prism<S, A>(val getOption: (S) -> A?, val reverseGet: (A) -> S) {
    operator fun invoke(a: A) = reverseGet(a)
    fun isMatching(s: S): Boolean = getOption(s) != null
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> modifyOption(ƒ)(s) ?: s }
    fun modifyOption(ƒ: (A) -> A): (S) -> S? = { s -> getOption(s)?.let { a -> reverseGet(ƒ(a)) } }
    infix fun <B> compose(other: Prism<A, B>): Prism<S, B> = Prism(
            getOption = { s -> getOption(s)?.let { a -> other.getOption(a) } },
            reverseGet = { b -> reverseGet(other.reverseGet(b)) }
    )
}

data class Lens<S, A>(val get: (S) -> A, val set: (A, S) -> S) {
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> set(ƒ(get(s)), s) }
    infix fun <B> compose(other: Lens<A, B>): Lens<S, B> = Lens(
            get = { s -> other.get(get(s)) },
            set = { b, s -> set(other.set(b, get(s)), s) }
    )
}

data class Optional<S, A>(val getOption: (S) -> A?, val set: (A, S) -> S) {
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> modifyOption(ƒ)(s) ?: s }
    fun modifyOption(ƒ: (A) -> A): (S) -> S? = { s -> getOption(s)?.let { a -> set(ƒ(a), s) } }
    infix fun <B> compose(other: Optional<A, B>): Optional<S, B> = Optional(
            getOption = { s -> getOption(s)?.let { a -> other.getOption(a) } },
            set = { b, s -> getOption(s)?.let { a -> set(other.set(b, a), s) } ?: s }
    )

    companion object {
        fun <A> index(i: Int) = Optional<List<A>, A>({ it.elementAtOrNull(i) }, { a, l -> l.mapIndexed { i2, a2 -> if (i == i2) a else a2 }})
        fun <A, S> index(key: A) = Optional<Map<A, S>, S>({ it[key] }, { a, l -> l + (key to a) })
    }
}

object Kotlens {
    fun <S, A> Iso<S, A>.toPrism(): Prism<S, A> = Prism(getOption = get, reverseGet = reverseGet)
    fun <S, A> Iso<S, A>.toLens(): Lens<S, A> = Lens(get = get, set = { a, _ -> reverseGet(a) })
    fun <S, A> Prism<S, A>.toOptional(): Optional<S, A> = Optional(getOption = getOption, set = { a, s -> modify { a }(s) })

    infix fun <S, A, B> Iso<S, A>.compose(other: Prism<A, B>): Prism<S, B> = toPrism() compose other
    infix fun <S, A, B> Prism<S, A>.compose(other: Iso<A, B>): Prism<S, B> = this compose other.toPrism()
    infix fun <S, A, B> Optional<S, A>.compose(other: Prism<A, B>): Optional<S, B> = this compose other.toOptional()
}

val todo: Nothing = throw NotImplementedError()

class Try<out T>(val ƒ: () -> T) {
    fun toOption(): T? = try { ƒ() } catch (t: Throwable) { null }
}