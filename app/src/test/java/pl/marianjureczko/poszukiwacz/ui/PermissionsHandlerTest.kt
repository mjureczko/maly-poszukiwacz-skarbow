package pl.marianjureczko.poszukiwacz.ui

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingTipPhoto

class PermissionsHandlerTest {

    @Test
    fun shouldReturnSecondIndex_whenAskingAboutNextOfTheFirstRequirement() {
        //given
        val permissionsToRequest = listOf(RequirementsForDoingTipPhoto)
        val permissionsHandler = PermissionsHandler(permissionsToRequest)

        //when
        val actual = permissionsHandler.requestNextPermission(RequirementsForDoingTipPhoto)

        //then
        assertThat(actual).isEqualTo(1)
    }

    @Test
    fun shouldReturnRequirements_whenRequestingExistingOne() {
        //given
        val permissionsToRequest = listOf(RequirementsForDoingTipPhoto)
        val permissionsHandler = PermissionsHandler(permissionsToRequest)

        //when
        val actual = permissionsHandler.getPermissionRequirements(0)

        //then
        assertThat(actual).isEqualTo(RequirementsForDoingTipPhoto)
    }

    @Test
    fun shouldReturnNull_whenRequestingNonExistingRequirements() {
        //given
        val permissionsToRequest = listOf(RequirementsForDoingTipPhoto)
        val permissionsHandler = PermissionsHandler(permissionsToRequest)

        //when
        val actual = permissionsHandler.getPermissionRequirements(1)

        //then
        assertThat(actual).isNull()
    }

}