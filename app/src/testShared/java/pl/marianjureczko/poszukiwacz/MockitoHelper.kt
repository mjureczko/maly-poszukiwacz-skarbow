package pl.marianjureczko.poszukiwacz

import org.mockito.Mockito

fun <T> any(type: Class<T>): T = Mockito.any(type)
fun <T> eq(type: T): T = Mockito.eq(type)