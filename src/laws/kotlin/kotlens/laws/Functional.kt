package kotlens.laws

// does this exist in stdlib?
infix fun<V, T, R> Function1<T, R>.compose(before: (V) -> T): (V) -> R {
    return { v: V -> this(before(v)) }
}