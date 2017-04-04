package kotlens

object Transformers {
    fun <S, A> Iso<S, A>.toPrism(): Prism<S, A> = Prism(getOption = get, reverseGet = reverseGet)
    fun <S, A> Iso<S, A>.toLens(): Lens<S, A> = Lens(get = get, set = { a, _ -> reverseGet(a) })
    fun <S, A> Prism<S, A>.toOptional(): Optional<S, A> = Optional(getOption = getOption, set = { a, s -> modify { a!! }(s) })

    infix fun <S, A, B> Iso<S, A>.compose(other: Prism<A, B>): Prism<S, B> = toPrism() compose other
    infix fun <S, A, B> Prism<S, A>.compose(other: Iso<A, B>): Prism<S, B> = this compose other.toPrism()
    infix fun <S, A, B> Optional<S, A>.compose(other: Prism<A, B>): Optional<S, B> = this compose other.toOptional()
}