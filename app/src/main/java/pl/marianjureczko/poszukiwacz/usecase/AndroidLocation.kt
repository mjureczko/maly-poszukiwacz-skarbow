package pl.marianjureczko.poszukiwacz.usecase

//TODO: create factory to get rid of all LocationWrapper usages in usecase
interface AndroidLocation {
    fun distanceTo(dest: AndroidLocation): Float
    var longitude: Double
    var latitude: Double

    /** Represents the estimated accuracy radius in meters */
    val accuracy: Float

    /**  The time when the location was observed in milliseconds since epoch. */
    val observedAt: Long
}
