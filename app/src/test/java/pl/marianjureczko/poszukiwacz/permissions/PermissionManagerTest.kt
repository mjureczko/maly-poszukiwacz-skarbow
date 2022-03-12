package pl.marianjureczko.poszukiwacz.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class PermissionManagerTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var activity: Activity

    @Mock
    lateinit var listener: PermissionListener

    @Test
    fun `SHOULD delegate checking permissions to ContextCompat checkSelfPermission() WHEN calling isPermissionGranted()`() {
        //given
        given(context.checkPermission(eq(PermissionsSpec.CAMERA.getPermissionsTextArray()[0]), anyInt(), anyInt()))
            .willReturn(PackageManager.PERMISSION_DENIED)

        //when
        val actual = PermissionManager.isPermissionGranted(context, PermissionsSpec.CAMERA)

        //then
        assertThat(actual).isFalse()
    }

    @Test
    fun `SHOULD delegate checking permissions to ContextCompat checkSelfPermission() WHEN calling areAllPermissionsGranted()`() {
        //given
        val permissions = RequirementsForPhotoAndAudioTip.getSpecsArray()

        //when
        val actual = PermissionManager.areAllPermissionsGranted(context, permissions)

        //then
        assertThat(actual)
            .`as`("when context mock is not configured it returns 0 which is PackageManager.PERMISSION_GRANTED")
            .isTrue()
        then(context).should().checkPermission(eq(permissions[0].getPermissionsTextArray()[0]), anyInt(), anyInt());
        then(context).should().checkPermission(eq(permissions[1].getPermissionsTextArray()[0]), anyInt(), anyInt());
    }

    @Test
    fun `SHOULD delegate to PermissionListener permissionsGranted() WHEN grant results show all granted`() {
        //given
        val manager = PermissionManager(listener)
        val requirements = RequirementsForPhotoAndAudioTip
        val fixture = PermissionsFixture(RequirementsForPhotoAndAudioTip)
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_GRANTED)

        //when
        manager.handleRequestPermissionsResult(activity, requirements, fixture.getAllPermissions(), grantResult)

        //then
        then(listener).should().permissionsGranted(requirements)
    }

    @Test
    fun `SHOULD show rationale dialog WHEN permissions not granted and rational should not be shown`() {
        //given
        val dialogs = mock(Dialogs::class.java)
        val manager = PermissionManager(listener, dialogs)
        val fixture = PermissionsFixture(RequirementsForPhotoAndAudioTip)
        val permissions = fixture.getAllPermissions()
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_DENIED)
        given(dialogs.shouldShowRationale(any(Activity::class.java), eq(permissions.toList())))
            .willReturn(true)

        //when
        manager.handleRequestPermissionsResult(activity, fixture.requirements, permissions, grantResult)

        //then
        then(dialogs).should().showPermissionRationaleDialog(activity, fixture.requirements, listener)
    }

    @Test
    fun `SHOULD show permanent denial dialog WHEN permissions not granted and rational should not be shown`() {
        //given
        val dialogs = mock(Dialogs::class.java)
        val manager = PermissionManager(listener, dialogs)
        val fixture = PermissionsFixture(RequirementsForPhotoAndAudioTip)
        val permissions = fixture.getAllPermissions()
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_DENIED)
        given(dialogs.shouldShowRationale(any(Activity::class.java), eq(permissions.toList())))
            .willReturn(false)

        //when
        manager.handleRequestPermissionsResult(activity, fixture.requirements, permissions, grantResult)

        //then
        then(dialogs).should().showPermissionPermanentDenialDialog(activity, fixture.requirements, listener)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any(type)
    private fun <T> eq(type: T): T = Mockito.eq(type)

}
