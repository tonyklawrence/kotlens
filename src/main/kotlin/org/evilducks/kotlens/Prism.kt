package org.evilducks.kotlens

class Prism<S, A>(val getOption: (S) -> A?, val reverseGet: (A) -> S)