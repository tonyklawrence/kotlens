package kotlens

fun Double.toIntOption(): Int? = if (this == toInt().toDouble()) toInt() else null
fun String.toIntOption(): Int? = Try { toInt() }.toOption()
fun String.toDoubleOption(): Double? = Try { toDouble() }.toOption()

private class Try<out T>(val ƒ: () -> T) {
    fun toOption(): T? = try { ƒ() } catch (t: Throwable) { null }
}