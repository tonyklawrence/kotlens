package org.evilducks.kotlens.examples

import org.evilducks.kotlens.Iso
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

data class Meter(val value: Double)
data class Yard(val value: Double)
data class Kilometer(val value: Double)
data class Mile(val value: Double)

class IsoComposition() {
    val meterToYard = Iso<Meter, Yard>({ (m) -> Yard(m * 1.09361) }, { (y) -> Meter(y / 1.09361)})
    val meterToKm = Iso<Meter, Kilometer>({ (m) -> Kilometer(m * 1000)}, { (km) -> Meter(km / 1000)})
    val yardToMile = Iso<Yard, Mile>({ (y) -> Mile(y / 1760) }, { (m) -> Yard(m * 1760) })

    @Test fun `composing isomorphisms`() {
        val kmToMile = meterToKm.reverse() combine meterToYard combine yardToMile
        assertThat(kmToMile.get(Kilometer(10.0)), equalTo(Mile(6.213693181818182E-6)))
    }
}