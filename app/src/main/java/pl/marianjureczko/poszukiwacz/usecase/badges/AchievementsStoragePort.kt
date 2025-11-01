package pl.marianjureczko.poszukiwacz.usecase.badges

interface AchievementsStoragePort {
    fun load(): Achievements?
    fun save(achievements: Achievements)
}