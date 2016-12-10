package kotlens

fun Double.toIntOption(): Int? = if (this == toInt().toDouble()) toInt() else null
fun String.toIntOption(): Int? = Try { toInt() }.toOption()
fun String.toDoubleOption(): Double? = Try { toDouble() }.toOption()
