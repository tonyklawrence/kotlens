package kotlens.examples

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.should.shouldMatch
import funk.toIntOption
import kotlens.Prism
import kotlens.examples.Day.Monday
import kotlens.examples.Day.Tuesday
import kotlens.examples.Json.JNumber
import kotlens.examples.Json.JString
import org.junit.Ignore
import org.junit.Test

class Prisms {

    @Test fun `day example`() {
        val tuesday = Prism<Day, Unit>({ if (it == Tuesday) Unit else null }, { Tuesday })

        tuesday.getOption(Monday) shouldMatch absent()

        tuesday.getOption(Tuesday) shouldMatch present()
        tuesday.getOption(Tuesday) shouldMatch equalTo(Unit)

        tuesday.reverseGet(Unit) shouldMatch isA(equalTo(Tuesday))
    }

    val jNum = Prism<Json, Double>({ if (it is JNumber) it.value else null }, ::JNumber)

    @Ignore("need kotlin 1.1 for data class inheritance")
    @Test fun `json example`() {
        jNum.modify { it + 1 }(JNumber(2.0)) shouldMatch isA(equalTo(JNumber(3.0)))
        jNum.modify { it + 1 }(JString("x")) shouldMatch isA(equalTo(JString("x")))

        jNum.modifyOption { it + 1 }(JNumber(2.0)) shouldMatch present(isA(equalTo(JNumber(3.0))))
        jNum.modifyOption { it + 1 }(JString("x")) shouldMatch absent()
    }

    val doubleToInt = Prism(Double::toIntOption, Int::toDouble)

    @Test fun `safe down casting`() {
        doubleToInt.getOption(3.4) shouldMatch absent()
        doubleToInt.getOption(3.0) shouldMatch equalTo(3)
        doubleToInt.reverseGet(5) shouldMatch equalTo(5.0)
    }

    @Test fun `composing prisms`() {
        val jInt: Prism<Json, Int> = jNum compose doubleToInt

        jInt.getOption(JNumber(3.0)) shouldMatch present(equalTo(3))
        jInt.getOption(JNumber(5.9)) shouldMatch absent()

        jInt.getOption(JString("9")) shouldMatch absent()
    }
}

sealed class Day {
    object Monday : Day()
    object Tuesday : Day()
}

sealed class Json {
    class JNumber(val value: Double) : Json()
    class JString(val value: String) : Json()
}