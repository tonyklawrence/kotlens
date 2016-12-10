package kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.junit.Test

class OptionalTest {
    data class Person(val name: String, val age: Int, val address: Address? = null)
    data class Address(val number: Int, val street: String)

    val tickleTown = Address(72, "Tickle Town")
    val mrTickle = Person("Mr Tickle", 34, tickleTown)

    @Test fun `optional properties`() {
        val optional = Optional<Person, Int>({ (_, age) -> age }, { age, person -> person.copy(age = age)})

        optional.getOption(mrTickle) shouldMatch equalTo(34)
        optional.set(100, mrTickle) shouldMatch equalTo(Person("Mr Tickle", 100, tickleTown))
    }

    @Test fun `composing optionals`() {
        val address = Optional<Person, Address>({ (_, _, address) -> address }, { address, person -> person.copy(address = address) })
        val street = Optional<Address, String>({ (_, street) -> street }, { street, address -> address.copy(street = street) })

        val personToStreet = address compose street
        personToStreet.getOption(mrTickle) shouldMatch equalTo("Tickle Town")

        val homelessJoe = Person("Homeless Joe", 70)

        address.getOption(homelessJoe) shouldMatch absent()
        personToStreet.getOption(homelessJoe) shouldMatch absent()
    }
}