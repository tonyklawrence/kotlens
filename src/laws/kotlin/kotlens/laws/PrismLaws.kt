package kotlens.laws

import funk.compose
import funk.toIntOption
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import kotlens.Prism

class PrismLaws : StringSpec() { init {
    val prism = Prism(String::toIntOption, Int::toString)

    "Partial round trip one way" {
        forAll { s: String ->
            prism.getOption(s)?.let { a -> prism.reverseGet(a) == s} ?: true
        }
    }

    "Round trip other way" {
        forAll { a: Int ->
            prism.getOption(prism.reverseGet(a)) == a
        }
    }

    "Modify identity" {
        forAll { s: String ->
            prism.modify{ it }(s) == s
        }
    }

    "Modify can compose" {
        val f = { a: Int -> a + 10 }
        val g = { a: Int -> a * 2 }

        forAll { s: String ->
            prism.modify(g)(prism.modify(f)(s)) == prism.modify(g compose f)(s)
        }
    }
}}