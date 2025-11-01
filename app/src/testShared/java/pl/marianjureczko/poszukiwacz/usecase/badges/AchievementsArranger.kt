package pl.marianjureczko.poszukiwacz.usecase.badges

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.rearrangerkt.Rearranger
import com.ocadotechnology.gembus.test.someObjects

class AchievementsArranger : CustomArranger<Achievements>() {
    override fun instance(): Achievements {
        val result = enhancedRandom.nextObject(Achievements::class.java)
        return Rearranger.copy(result) {
            Achievements::badges set someObjects<Badge>(3).toList()
        }
    }
}