package kotlens.examples

import kotlens.Iso
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

data class Meter(val value: Double)
data class Yard(val value: Double)
data class Kilometer(val value: Double)
data class Mile(val value: Double)

class Isomorphisms {
    val meterToYard = Iso<Meter, Yard>({ Yard(it.value * 1.09361) }, { Meter(it.value / 1.09361) })
    val meterToKm = Iso<Meter, Kilometer>({ Kilometer(it.value * 1000) }, { Meter(it.value / 1000) })
    val yardToMile = Iso<Yard, Mile>({ Mile(it.value / 1760) }, { Yard(it.value * 1760) })

    @Test fun `composing isomorphisms`() {
        val kmToMile = meterToKm.reverse() compose meterToYard compose yardToMile
        assertThat(kmToMile.get(Kilometer(10.0)), equalTo(Mile(6.213693181818182E-6)))

        val yardToYard = meterToYard.reverse() compose meterToKm compose kmToMile compose yardToMile.reverse()
        assertThat(yardToYard.get(Yard(1.0)), equalTo(Yard(1.0)))
    }
}