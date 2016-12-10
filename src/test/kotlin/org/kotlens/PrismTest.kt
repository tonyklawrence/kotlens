package org.kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test
import org.kotlens.Kotlens.compose

class PrismTest {
    val prism = Prism(String::toIntOption, Int::toString)

    @Test fun prism() {
        prism.getOption("100") shouldMatch equalTo(100)
        prism.getOption("nan") shouldMatch absent()
        prism.getOption(prism.reverseGet(100)) shouldMatch equalTo(100)
        prism.reverseGet(100) shouldMatch equalTo("100")
    }

    @Test fun `can determine if a prism matches`() {
        prism.isMatching("100") shouldMatch equalTo(true)
        prism.isMatching("bad") shouldMatch equalTo(false)
    }

    @Test fun `if possible we can modify using the prism`() {
        prism.modify(double)("100") shouldMatch equalTo("200")
        prism.modify(double)("what?") shouldMatch equalTo("what?")
    }

    @Test fun `try to optionally modify using the prism`() {
        prism.modifyOption(double)("5") shouldMatch equalTo("10")
        prism.modifyOption(double)("no") shouldMatch absent()
    }

    @Test fun `prisms can be composed`() {
        val a2b = Prism(String::toDoubleOption, Double::toString)
        val b2c = Prism(Double::toIntOption, Int::toDouble)
        val a2c = a2b compose b2c

        a2c.getOption("100.0") shouldMatch equalTo(100)
        a2c.reverseGet(100) shouldMatch equalTo("100.0")
        a2c.modify(double)("3.0") shouldMatch equalTo("6.0")
        a2c.modify(double)("oops") shouldMatch equalTo("oops")
        a2c.modify(double)("1.23") shouldMatch equalTo("1.23")
    }

    @Test fun `can compose a prism with an iso`() {
        val a2b = Prism(String::toDoubleOption, Double::toString)
        val b2c = Iso(Double::toInt, Int::toDouble)
        val a2c = a2b compose b2c

        a2c.getOption("100.0") shouldMatch equalTo(100)
        a2c.reverseGet(100) shouldMatch equalTo("100.0")
        a2c.modify(double)("1.23") shouldMatch equalTo("2.0")
    }

    private val double: (Int) -> Int = { it * 2 }
}