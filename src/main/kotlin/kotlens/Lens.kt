package kotlens

data class Lens<S, A>(val get: (S) -> A, val set: (A, S) -> S) {
    fun modify(ƒ: (A) -> A): (S) -> S = { s -> set(ƒ(get(s)), s) }
    infix fun <B> compose(other: Lens<A, B>): Lens<S, B> = Lens(
            get = { s -> other.get(get(s)) },
            set = { b, s -> set(other.set(b, get(s)), s) }
    )
}