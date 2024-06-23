import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.googleGmsGoogleServices)
  alias(libs.plugins.kotlinKsp)
}

kotlin {
  sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata")
  }
  sourceSets.androidMain {
    kotlin.srcDir("build/generated/ksp/metadata")
  }
  sourceSets.iosMain {
    kotlin.srcDir("build/generated/ksp/metadata")
  }

  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    freeCompilerArgs.add("-Xcontext-receivers")
    freeCompilerArgs.add("-Xenable-builder-inference")
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }

  jvm("desktop")

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      isStatic = true
    }
  }

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      implementation(libs.compose.ui.tooling.preview)
      implementation(libs.androidx.activity.compose)
      implementation(project.dependencies.platform(libs.firebase.bom))
      implementation(libs.androidx.work.runtime.ktx)
      implementation(libs.firebase.messaging.ktx)
      implementation(libs.firebase.auth.ktx)
      implementation(libs.androidx.credentials)
      implementation(libs.androidx.credentials.play.services.auth)
      implementation(libs.googleid)
    }

    commonMain.dependencies {
      implementation(projects.shared)
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(libs.koalaplot.core)
      implementation(libs.arrow.optics)
    }

    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}

android {
  namespace = "org.acv.hopla"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  defaultConfig {
    applicationId = "org.acv.hopla"
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
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  dependencies {
    debugImplementation(libs.compose.ui.tooling)
  }
}

dependencies {

  add("kspCommonMainMetadata", libs.arrow.optics.ksp)
  add("kspAndroid", libs.arrow.optics.ksp)
  add("kspIosX64", libs.arrow.optics.ksp)
  add("kspIosArm64", libs.arrow.optics.ksp)
  add("kspIosSimulatorArm64", libs.arrow.optics.ksp)
}

//tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
//  if (name != "kspCommonMainKotlinMetadata") {
//    dependsOn("kspCommonMainKotlinMetadata")
//  }
//}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "org.acv.hopla"
      packageVersion = "1.0.0"
    }
  }
}