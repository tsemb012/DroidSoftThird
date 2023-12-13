plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
    id("com.cookpad.android.plugin.license-tools") version "1.2.8"
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.1"
    }

    defaultConfig {
        applicationId = "com.tsemb.droidsoftthird"
        minSdk = 24
        targetSdk = 33
        versionCode = 5
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    lintOptions {
        isCheckDependencies = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    dataBinding {
        enable = true
    }
}

dependencies {
    val kotlin_version = "1.8.0"


    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //-----Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //-----JunitTest
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    //-----Kotest
    val kotest_version = "5.5.4"
    testImplementation("io.kotest:kotest-framework-engine:$kotest_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")

    //-----MockK
    val mockk_version = "1.13.2"
    testImplementation("io.mockk:mockk:$mockk_version")

    //-----Jitsi meet
    implementation ("org.jitsi.react:jitsi-meet-sdk:8.1.2") { isTransitive = true }

    //-----androidx

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.annotation:annotation:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Java language implementation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")

    // Kotlin
    val nav_version = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:2.4.0-beta02")

    //-----FireBase
    implementation("com.google.firebase:firebase-firestore:24.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.firebaseui:firebase-ui-storage:6.4.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage:20.1.0")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    //-----Viewpager2の依存関係
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.6.0-alpha01")
    implementation("com.firebaseui:firebase-ui-auth:6.4.0")

    //-----CardView
    implementation("androidx.cardview:cardview:1.0.0")

    //-----Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.4.0")

    //-----Coil
    val coil_version = "2.3.0"
    implementation("io.coil-kt:coil:$coil_version")
    implementation("io.coil-kt:coil-compose:$coil_version")

    //-----Glide
    //annotationProcessorn("com.github.bumptech.glide:compiler:4.11.0"
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    //-----Hilt
    val hilt_version = "2.45"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //-----Hilt For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.43.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.44.1")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.44")

    //-----Hilt For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.43.1")
    kaptTest("com.google.dagger:hilt-compiler:2.44.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.44")

    //----Dagger2
    kaptAndroidTest("com.squareup.inject:assisted-inject-processor-dagger2:0.6.0")
    implementation("com.google.dagger:dagger:2.44.2")
    //kaptn("com.google.dagger:dagger-compiler:2.44.2"

    //-----Retrofit
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")

    //-----Moshi
    val moshi_version = "1.14.0"
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")

    //-----OkHttp
    val okhttp_version = "4.10.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    //-----JetpackCompose
    val jetpack_compose_version = "1.4.0"
    implementation("androidx.compose.ui:ui:$jetpack_compose_version")
    implementation("androidx.compose.ui:ui-tooling:$jetpack_compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$jetpack_compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$jetpack_compose_version")
    implementation("androidx.compose.foundation:foundation:1.5.0-alpha04")
    implementation("androidx.compose.material:material:$jetpack_compose_version")
    implementation("androidx.compose.material:material-icons-core:$jetpack_compose_version")
    implementation("androidx.compose.material:material-icons-extended:$jetpack_compose_version")
    implementation("androidx.compose.runtime:runtime-livedata:$jetpack_compose_version")
    implementation("androidx.compose.runtime:runtime-rxjava2:$jetpack_compose_version")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    //----- Material3
    implementation("androidx.compose.material3:material3:1.0.1")

    //----- Preferences DataStore (SharedPreferences like APIs)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")

    //-----Room
    //implementation("androidx.room:room-ktx:$room_version"

    //-----Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    //-----SeekBar
    implementation("org.adw.library:discrete-seekbar:1.0.1")

    //-----CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //-----PermissionDispatcher
    implementation("com.github.permissions-dispatcher:permissionsdispatcher:4.8.0")
    kapt("com.github.permissions-dispatcher:permissionsdispatcher-processor:4.8.0")

    //-----Dexter
    implementation("com.karumi:dexter:6.2.2")

    //-----EventBus of GreenRobot
    implementation("org.greenrobot:eventbus:3.2.0")

    //-----Stfalcon ImageViewer
    implementation("com.github.stfalcon:stfalcon-imageviewer:1.0.0")

    //----- GoogleMaps for Android
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.android.libraries.places:places:3.0.0")
    implementation("com.google.maps.android:maps-compose:2.7.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    //-----CalendarView
    implementation("com.github.kizitonwose:CalendarView:1.0.4")

    //-----CalendarView
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    //-----DataBinding-ktx
    implementation("com.github.wada811:DataBinding-ktx:5.0.0")

    //-----MaterialDateTimeDialog
    implementation("com.afollestad.material-dialogs:datetime:3.2.1")

    //-----MaterialDateLifeCycle
    implementation("com.afollestad.material-dialogs:lifecycle:3.3.0")

    //-----SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //-----OpenStreetMap
    implementation("org.osmdroid:osmdroid-android:6.1.15")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")

    //-----UCrop
    implementation("com.github.yalantis:ucrop:2.2.6-native")

    //-----Paging
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

    //-----ThreeTenABP
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")

    //-----Google Fonts
    implementation("com.google.android.material:compose-theme-adapter:1.2.1")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.4.3")
}