package org.evilducks.kotlens

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class IsoTest {

    @Test fun isomorphism() {
        val iso = Iso(String::reversed, String::reversed)

        assertThat(iso.get("String"), equalTo("gnirtS"))
        assertThat(iso.reverseGet(iso.get("String")), equalTo("String"))
        assertThat(iso.get(iso.reverseGet("Anything")), equalTo("Anything"))
    }

    @Test fun `we can reverse an iso`() {
        val iso = Iso(String::toInt, Int::toString)
        val reversed = iso.reverse()

        assertThat(iso.get("100"), equalTo(100))
        assertThat(reversed.get(100), equalTo("100"))
    }
}