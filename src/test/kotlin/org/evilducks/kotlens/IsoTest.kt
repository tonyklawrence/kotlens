package org.evilducks.kotlens

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class IsoTest {

    @Test fun isomorphism() {
        val iso = Iso(String::reversed, String::reversed)

        assertThat(iso.reverseGet(iso.get("S")), equalTo("S"))
        assertThat(iso.get(iso.reverseGet("A")), equalTo("A"))
    }
}