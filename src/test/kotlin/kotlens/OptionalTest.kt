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
        val optional = Optional<Person, Int>({ it.age }, { age, person -> age?.let { person.copy(age = age) } ?: person })

        optional.getOption(mrTickle) shouldMatch equalTo(34)
        optional.set(100, mrTickle) shouldMatch equalTo(Person("Mr Tickle", 100, tickleTown))
    }

    @Test fun `composing optionals`() {
        val address = Optional<Person, Address>({ it.address }, { address, person -> person.copy(address = address) })
        val street = Optional<Address, String>({ it.street }, { street, address -> street?.let { address.copy(street = street) } ?: address })

        val personToStreet = address compose street
        personToStreet.getOption(mrTickle) shouldMatch equalTo("Tickle Town")

        val homelessJoe = Person("Homeless Joe", 70)

        address.getOption(homelessJoe) shouldMatch absent()
        personToStreet.getOption(homelessJoe) shouldMatch absent()
    }

    @Test fun `can access list using index`() {
        val second = Optional.index<Int>(1)

        second.getOption(emptyList()) shouldMatch absent()
        second.getOption(listOf(1)) shouldMatch absent()
        second.getOption(listOf(1, 2, 3)) shouldMatch equalTo(2)
    }

    @Test fun `can update a list using index`() {
        val second = Optional.index<Int>(1)

        second.set(0, emptyList()) shouldMatch equalTo(emptyList())
        second.set(0, listOf(1)) shouldMatch equalTo(listOf(1))
        second.set(0, listOf(1, 2, 3)) shouldMatch equalTo(listOf(1, 0, 3))
    }

    @Test fun `does not remove from index`() {
        val second = Optional.index<Int>(1)

        second.set(null, emptyList()) shouldMatch equalTo(emptyList())
        second.set(null, listOf(1)) shouldMatch equalTo(listOf(1))
        second.set(null, listOf(1, 2, 3)) shouldMatch equalTo(listOf(1, 2, 3))
    }

    @Test fun `can access map using at`() {
        val name = Optional.at<String, String>("name")

        name.getOption(emptyMap()) shouldMatch absent()
        name.getOption(mapOf("foo" to "bar")) shouldMatch absent()
        name.getOption(mapOf("name" to "value")) shouldMatch equalTo("value")
    }

    @Test fun `can update map using at`() {
        val name = Optional.at<String, String>("name")

        name.set("value", emptyMap()) shouldMatch equalTo(mapOf("name" to "value"))
        name.set("value", mapOf("foo" to "bar")) shouldMatch equalTo(mapOf("foo" to "bar", "name" to "value"))
        name.set("value", mapOf("name" to "bar")) shouldMatch equalTo(mapOf("name" to "value"))
    }

    @Test fun `can remove from map using at`() {
        val name = Optional.at<String, String>("name")

        name.set(null, emptyMap()) shouldMatch equalTo(emptyMap())
        name.set(null, mapOf("foo" to "bar")) shouldMatch equalTo(mapOf("foo" to "bar"))
        name.set(null, mapOf("name" to "value")) shouldMatch equalTo(emptyMap())
        name.set(null, mapOf("foo" to "bar", "name" to "value")) shouldMatch equalTo(mapOf("foo" to "bar"))
    }
}