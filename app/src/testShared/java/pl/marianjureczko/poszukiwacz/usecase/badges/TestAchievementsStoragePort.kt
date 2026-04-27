package pl.marianjureczko.poszukiwacz.usecase.badges

class TestAchievementsStoragePort : AchievementsStoragePort {
    private var achievements: Achievements? = null

    override fun loadAchievements(): Achievements? {
        return achievements
    }

    override fun save(achievements: Achievements) {
        this.achievements = achievements
    }
}