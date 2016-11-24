package org.evilducks.kotlens

import org.junit.Test

class OptionalTest {

    @Test fun `optional properties`() {
        val optional = Optional(String::toIntOption, { a, s -> s })
    }
}