package org.evilducks.kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class PrismTest() {

    @Test fun prism() {
        val prism = Prism({ it.toIntOption() }, Int::toString)

        prism.getOption("100") shouldMatch equalTo(100)
        prism.getOption("nan") shouldMatch absent()
        prism.getOption(prism.reverseGet(100)) shouldMatch equalTo(100)
    }

    private fun String.toIntOption(): Int? = Try { toInt() }.toOption()
}