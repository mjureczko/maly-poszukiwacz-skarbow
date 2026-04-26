# The project uses Build Variants

Build variants (https://developer.android.com/build/build-variants) are used to create different variants of the app.
It's a solution based on product flavors.
On top of the predefined `release` and `debug` flavors are defined the following custom flavors:

- classic
    - defaultAssets
- custom
    - kalinowice
    - ...

That leads, thanks to the `variantFilter` configuration as well as `mode` and `assets` dimension from build.gradle, to
the following variants:

- kalinowiceCustomDebug
- kalinowiceCustomRelease
- defaultAssetsClassicDebug
- defaultAssetsClassicRelease

In order to refer to one of the variant, for instance to execute unit tests, one need to execute:

```
$ ./gradlew testKalinowiceCustomDebugUnitTest
```

# Source sets

For each flavor there has been created a source set with the same name as the flavor.
There are also dedicated test source sets with the `test` prefix, i.e. `test<flavor>`
Note that the source sets contains not only source code but also resources and assets.
The pattern is not followed for the instrumented UI tests.
For those tests, the source set directory name has and `androidTest` prefix followed by all flavors required to have a
vaild build variant, e.g. `androidTestKalinowiceCustomDebug`.
