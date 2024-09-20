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
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class PermissionManagerTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var activity: Activity

    @Mock
    lateinit var listener: pl.marianjureczko.poszukiwacz.permissions.PermissionListener

    @Test
    fun `SHOULD delegate checking permissions to ContextCompat checkSelfPermission() WHEN calling isPermissionGranted()`() {
        //given
        given(context.checkPermission(pl.marianjureczko.poszukiwacz.eq(pl.marianjureczko.poszukiwacz.permissions.PermissionsSpec.CAMERA.getPermissionsTextArray()[0]), anyInt(), anyInt()))
            .willReturn(PackageManager.PERMISSION_DENIED)

        //when
        val actual = pl.marianjureczko.poszukiwacz.permissions.PermissionManager.isPermissionGranted(context, pl.marianjureczko.poszukiwacz.permissions.PermissionsSpec.CAMERA)

        //then
        assertThat(actual).isFalse()
    }

    @Test
    fun `SHOULD delegate checking permissions to ContextCompat checkSelfPermission() WHEN calling areAllPermissionsGranted()`() {
        //given
        val permissions = pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip.getSpecsArray()

        //when
        val actual = pl.marianjureczko.poszukiwacz.permissions.PermissionManager.areAllPermissionsGranted(context, permissions)

        //then
        assertThat(actual)
            .`as`("when context mock is not configured it returns 0 which is PackageManager.PERMISSION_GRANTED")
            .isTrue()
        then(context).should().checkPermission(pl.marianjureczko.poszukiwacz.eq(permissions[0].getPermissionsTextArray()[0]), anyInt(), anyInt());
        then(context).should().checkPermission(pl.marianjureczko.poszukiwacz.eq(permissions[1].getPermissionsTextArray()[0]), anyInt(), anyInt());
    }

    @Test
    fun `SHOULD delegate to PermissionListener permissionsGranted() WHEN grant results show all granted`() {
        //given
        val manager = pl.marianjureczko.poszukiwacz.permissions.PermissionManager(listener)
        val requirements = pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip
        val fixture = PermissionsFixture(pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip)
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_GRANTED)

        //when
        manager.handleRequestPermissionsResult(activity, requirements, fixture.getAllPermissions(), grantResult)

        //then
        then(listener).should().permissionsGranted(requirements)
    }

    @Test
    fun `SHOULD show rationale dialog WHEN permissions not granted and rational should not be shown`() {
        //given
        val dialogs = mock(pl.marianjureczko.poszukiwacz.permissions.Dialogs::class.java)
        val manager = pl.marianjureczko.poszukiwacz.permissions.PermissionManager(listener, dialogs)
        val fixture = PermissionsFixture(pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip)
        val permissions = fixture.getAllPermissions()
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_DENIED)
        given(dialogs.shouldShowRationale(pl.marianjureczko.poszukiwacz.any(Activity::class.java),
            pl.marianjureczko.poszukiwacz.eq(permissions.toList())
        ))
            .willReturn(true)

        //when
        manager.handleRequestPermissionsResult(activity, fixture.requirements, permissions, grantResult)

        //then
        then(dialogs).should().showPermissionRationaleDialog(activity, fixture.requirements, listener)
    }

    @Test
    fun `SHOULD show permanent denial dialog WHEN permissions not granted and rational should not be shown`() {
        //given
        val dialogs = mock(pl.marianjureczko.poszukiwacz.permissions.Dialogs::class.java)
        val manager = pl.marianjureczko.poszukiwacz.permissions.PermissionManager(listener, dialogs)
        val fixture = PermissionsFixture(pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip)
        val permissions = fixture.getAllPermissions()
        val grantResult = fixture.getGrantResults(PackageManager.PERMISSION_DENIED)
        given(dialogs.shouldShowRationale(pl.marianjureczko.poszukiwacz.any(Activity::class.java),
            pl.marianjureczko.poszukiwacz.eq(permissions.toList())
        ))
            .willReturn(false)

        //when
        manager.handleRequestPermissionsResult(activity, fixture.requirements, permissions, grantResult)

        //then
        then(dialogs).should().showPermissionPermanentDenialDialog(activity, fixture.requirements, listener)
    }
}
