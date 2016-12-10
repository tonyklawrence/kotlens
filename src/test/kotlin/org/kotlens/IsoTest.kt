package org.kotlens

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class IsoTest {

    @Test fun isomorphism() {
        val iso = Iso(String::reversed, String::reversed)

        iso.get("String") shouldMatch equalTo("gnirtS")
        iso.reverseGet(iso.get("String")) shouldMatch equalTo("String")
        iso.get(iso.reverseGet("Anything")) shouldMatch equalTo("Anything")
    }

    @Test fun `modification via iso should be the same as without`() {
        val iso = Iso(String::toInt, Int::toString)
        val f = { n: Int -> n + 1 }

        val x = "10"
        iso.modify(f)(x) shouldMatch equalTo(iso.reverseGet(f(iso.get(x))))
    }

    @Test fun `we can reverse an iso`() {
        val iso = Iso(String::toInt, Int::toString)
        val reversed = iso.reverse()

        iso.get("100") shouldMatch equalTo(100)
        reversed.get(100) shouldMatch equalTo("100")
    }

    @Test fun `isomorphisms can be composed`() {
        val a2b = Iso(String::toInt, Int::toString)
        val b2c = Iso(Int::toDouble, Double::toInt)
        val a2c = a2b compose b2c

        a2c.get("100") shouldMatch equalTo(100.0)
        a2c.reverseGet(100.0) shouldMatch equalTo("100")
    }
}