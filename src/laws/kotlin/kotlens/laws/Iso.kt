package kotlens.laws

import io.kotlintest.specs.StringSpec

class Iso: StringSpec() {

    init {
        "strings.length should return size of string" {
            "hello".length shouldBe 5
        }
    }
}