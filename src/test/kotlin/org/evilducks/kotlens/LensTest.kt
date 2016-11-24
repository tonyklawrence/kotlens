package org.evilducks.kotlens

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class LensTest() {

    @Test fun `lenses`() {
        val lens = Lens(Person::age, { age, person -> person.copy(age = age) })

        val mrTickle = Person("Mr Tickle", 34)

        lens.get(mrTickle) shouldMatch equalTo(34)
        lens.set(100, mrTickle) shouldMatch equalTo(Person("Mr Tickle", 100))
        lens.modify { it + 1 }(mrTickle) shouldMatch equalTo(Person("Mr Tickle", 35))
    }
}

data class Person(val name: String, val age: Int)