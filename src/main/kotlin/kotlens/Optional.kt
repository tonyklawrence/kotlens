package kotlens

data class Optional<S, A>(val getOption: (S) -> A?, val set: (A?, S) -> S) {
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> modifyOption(ƒ)(s) ?: s }
    fun modifyOption(ƒ: (A) -> A): (S) -> S? = { s -> getOption(s)?.let { a -> set(ƒ(a), s) } }

    infix fun <B> compose(other: Optional<A, B>): Optional<S, B> = Optional(
            getOption = { s -> getOption(s)?.let { a -> other.getOption(a) } },
            set = { b, s -> getOption(s)?.let { a -> set(other.set(b, a), s) } ?: s }
    )

    companion object {
        fun <A> index(i: Int) = Optional<List<A>, A>({ it.elementAtOrNull(i) }, { a, l ->
            a?.let { l.mapIndexed { i2, a2 -> if (i == i2) a else a2 } } ?: l
        })
        fun <A, S> at(key: A) = Optional<Map<A, S>, S>({ it[key] }, { a, l ->
            a?.let { l + (key to a) } ?: l.filterKeys { it != key }
        })
    }
}