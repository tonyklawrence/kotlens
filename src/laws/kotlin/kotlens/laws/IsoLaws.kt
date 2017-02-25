package kotlens.laws

import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import kotlens.Iso

class IsoLaws : StringSpec() { init {
    val iso = Iso(String::toList, { a -> a.joinToString("") })

    "Round trip one way" {
        forAll { s: String ->
            (iso compose iso.reverse()).get(s) == s
        }
    }

    "Round trip other way" {
        forAll(Gen.list(Gen.char()), { a ->
            (iso.reverse() compose iso).get(a) == a
        })
    }

    "Modify with identity" {
        forAll { s: String ->
            iso.modify { a -> a }(s) == s
        }
    }

    "Modify can compose" {
        // does this exist in stdlib?
        infix fun<V, T, R> Function1<T, R>.compose(before: (V) -> T): (V) -> R {
            return { v: V -> this(before(v)) }
        }

        val f = { a: List<Char> -> a.filterIndexed { i, c -> i % 3 == 2 } }
        val g = { a: List<Char> -> a.filterIndexed { i, c -> i % 5 == 0 } }

        forAll { s: String ->
            iso.modify(f)(iso.modify(g)(s)) == iso.modify(f compose g)(s)
        }
    }
}}