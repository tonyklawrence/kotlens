package kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import kotlens.Kotlens.compose
import org.junit.Test

class CompositionTest {

    @Test fun `can compose isomorphisms with other optics`() {
        val iso = Iso(String::reversed, String::reversed)
        val prism = Prism(String::toIntOption, Int::toString)

        val isoToPrism = iso compose prism

        isoToPrism.getOption("123") shouldMatch equalTo(321)
        isoToPrism.getOption("no!") shouldMatch absent()
        isoToPrism.reverseGet(800) shouldMatch equalTo("008")
    }
}