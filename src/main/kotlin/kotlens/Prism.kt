package kotlens

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