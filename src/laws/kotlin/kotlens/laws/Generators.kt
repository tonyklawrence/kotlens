package kotlens.laws

import io.kotlintest.properties.Gen

fun Gen.Companion.char(): Gen<Char> = object : Gen<Char> {
    override fun generate(): Char = nextPrintableString(1).first()
}