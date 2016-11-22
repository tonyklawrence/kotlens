package org.evilducks.kotlens

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class PrismTest() {

    @Test fun prism() {
        val prism = Prism({ Try{ it.toInt() }.toOption() }, Int::toString)

        assertThat(prism.getOption("100"), equalTo(100))
        prism.getOption("nan")
        assertThat(prism.getOption(prism.reverseGet(100)), equalTo(100))
    }
}