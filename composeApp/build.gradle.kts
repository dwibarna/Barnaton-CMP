import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

val generatedAppConfigDir = layout.buildDirectory.dir("generated/appconfig/commonMain/kotlin")

val generateAppConfig = tasks.register("generateAppConfig") {
    val localPropsFile = rootProject.file("local.properties")
    val apiKeyProvider = providers.provider {
        val props = Properties()
        if (localPropsFile.exists()) {
            localPropsFile.inputStream().use { props.load(it) }
        }
        props.getProperty("TMDB_API_KEY") ?: System.getenv("TMDB_API_KEY") ?: ""
    }
    inputs.property("tmdbApiKey", apiKeyProvider)
    outputs.dir(generatedAppConfigDir)

    doLast {
        val rawKey = apiKeyProvider.get()
        val escapedKey = rawKey.replace("\\", "\\\\").replace("\"", "\\\"")
        val outFile = generatedAppConfigDir.get().file("utils/AppConfig.kt").asFile
        outFile.parentFile.mkdirs()
        outFile.writeText(
            """
            package utils

            object AppConfig {
                const val BASE_URL: String = "https://api.themoviedb.org/3/"
                const val TMDB_API_KEY: String = "$escapedKey"
            }
            """.trimIndent() + "\n"
        )
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.media3.exoplayer)
            implementation(libs.media3.ui)
            implementation(libs.media3.exoplayer.hls)
        }

        commonMain {
            kotlin.srcDir(generateAppConfig)
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.core)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.websockets)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.lifecycle.viewmodel.compose)
                implementation(libs.lifecycle.runtime.compose)
                implementation(libs.navigation.compose)
            }
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.barna.barnaton"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.barna.barnaton"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}
