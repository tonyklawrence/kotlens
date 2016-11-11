package org.evilducks.kotlens

data class Iso<S, A>(val get: (S) -> A, val reverseGet: (A) -> S)