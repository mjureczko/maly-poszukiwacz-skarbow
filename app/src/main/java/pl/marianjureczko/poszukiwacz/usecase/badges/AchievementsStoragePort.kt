package pl.marianjureczko.poszukiwacz.usecase.badges

interface AchievementsStoragePort {
    fun loadAchievements(): Achievements?
    fun save(achievements: Achievements)
}