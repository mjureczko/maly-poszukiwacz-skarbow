package pl.marianjureczko.poszukiwacz.permissions

interface Requirements {
    fun getPermission(): String?
    fun getMessage(): Int
    fun shouldRequestOnThiDevice(): Boolean
}