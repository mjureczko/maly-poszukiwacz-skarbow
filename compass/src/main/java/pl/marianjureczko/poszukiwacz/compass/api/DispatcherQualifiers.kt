package pl.marianjureczko.poszukiwacz.compass.api

import javax.inject.Qualifier

/**
 * When using the compass module a dispatcher with this qualifier must be delivered to Hilt.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CompassIoDispatcher