plugins {
    kotlin("multiplatform") version "1.4.0-rc"
}

val kotlinVersion = "1.4.0-rc"
val reactVersion = "16.13.1"


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */
    js {
        browser {}
        useCommonJs()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        js().compilations["main"].defaultSourceSet {
            dependencies {

                // Kotlin-react
                implementation("org.jetbrains:kotlin-react:$reactVersion-pre.110-kotlin-$kotlinVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion-pre.110-kotlin-$kotlinVersion")
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))

                // editor
                implementation(npm("codemirror", "5.56.0"))
                implementation(npm("react-codemirror2", "7.2.1"))

                // sql.js
                implementation(npm("sql.js", "1.3.0"))
                implementation(npm("copy-webpack-plugin", "6.0.3"))

                // css
                implementation(npm("style-loader", "1.2.1"))
                implementation(npm("css-loader", "4.2.1"))

                // corutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")
            }
        }
    }
}