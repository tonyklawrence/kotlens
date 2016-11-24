package org.evilducks.kotlens

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class LensTest {
    val tickleTown = Address(72, "Tickle Town")
    val mrTickle = Person("Mr Tickle", 34, tickleTown)

    @Test fun `lenses`() {
        val age = Lens<Person, Int>({ (name, age) -> age }, { age, person -> person.copy(age = age) })

        age.get(mrTickle) shouldMatch equalTo(34)
        age.set(100, mrTickle) shouldMatch equalTo(Person("Mr Tickle", 100, tickleTown))
        age.modify { it + 1 }(mrTickle) shouldMatch equalTo(Person("Mr Tickle", 35, tickleTown))
    }

    @Test fun `composing lenses`() {
        val address = Lens<Person, Address>({ (name, age, address) -> address }, { address, person -> person.copy(address = address)})
        val street = Lens<Address, String>({ (_, street) -> street }, { street, address -> address.copy(street = street)})

        (address compose street).get(mrTickle) shouldMatch equalTo("Tickle Town")
        (address compose street).set("FunnyVille", mrTickle) shouldMatch equalTo(Person("Mr Tickle", 34, Address(72, "FunnyVille")))
    }
}

data class Person(val name: String, val age: Int, val address: Address)
data class Address(val number: Int, val street: String)