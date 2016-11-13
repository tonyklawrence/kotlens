package org.evilducks.kotlens

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class PrismTest() {

    @Test fun prism() {
        val prism = Prism(String::toInt, Int::toString)

        assertThat(prism.getOption("100"), equalTo(100))
        prism.getOption("100")?.let { assertThat(prism.reverseGet(it), equalTo("100"))}
        assertThat(prism.getOption(prism.reverseGet(100)), equalTo(100))
    }
}