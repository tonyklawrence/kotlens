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
}}