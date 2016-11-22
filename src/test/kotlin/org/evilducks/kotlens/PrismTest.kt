package org.evilducks.kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class PrismTest() {
    val prism = Prism({ it.toIntOption() }, Int::toString)

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

    private val double: (Int) -> Int = { it * 2 }
    private fun String.toIntOption(): Int? = Try { toInt() }.toOption()
}