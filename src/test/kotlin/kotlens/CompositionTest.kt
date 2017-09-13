package kotlens

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import funk.toIntOption
import kotlens.Transformers.compose
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

    @Test fun `can compose lenses with other optics`() {
        val lens = Lens<Data, List<Int>>({ it.numbers }, { numbers, data -> data.copy(numbers = numbers) })
        val optional = Optional.index<Int>(2)

        val lensToOptional = lens compose optional

        lensToOptional.getOption(Data(listOf(2, 4, 6, 8, 10))) shouldMatch equalTo(6)
        lensToOptional.getOption(Data(emptyList())) shouldMatch absent()
        lensToOptional.set(9, Data(listOf(1, 2, 3, 4, 5))) shouldMatch equalTo(Data(listOf(1, 2, 9, 4, 5)))
        lensToOptional.set(9, Data(emptyList())) shouldMatch equalTo(Data(emptyList()))
    }

    private data class Data(val numbers: List<Int>)
}