/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.5/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("multiplatform")
    `maven-publish`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}


kotlin {
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useSourceMapSupport()
                    useChromeHeadless()
                }
            }
        }
    }
    jvm {}

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
//                implementation(KotlinX.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Testing.kotest.assertions.core)
                implementation(KotlinX.coroutines.core)
                implementation(KotlinX.datetime)
                implementation(npm("@js-joda/timezone", "_"))
//                implementation(npm("@js-joda/locale", "_"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("@fluent/bundle", "_"))
                implementation(npm("@fluent/sequence", "_"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("net.xyzsd.fluent:fluent-base:_")
                implementation("net.xyzsd.fluent:fluent-functions-cldr:_")
                implementation("net.xyzsd.fluent:fluent-functions-icu:_")
            }
        }
        val jvmTest by getting {
            dependencies {
                runtimeOnly("org.junit.jupiter:junit-jupiter:_")
                implementation(kotlin("test-junit"))
            }
        }
    }
}

configure<PublishingExtension> {
    repositories {
        logger.info("configuring publishing")
        if (project.hasProperty("publish")) {
            maven {
                // this is what we do in github actions
                // GOOGLE_APPLICATION_CREDENTIALS env var must be set for this to work
                // either to a path with the json for the service account or with the base64 content of that.
                // in github actions we should configure a secret on the repository with a base64 version of a service account
                // export GOOGLE_APPLICATION_CREDENTIALS=$(cat /Users/jillesvangurp/.gcloud/jvg-admin.json | base64)

                url = uri("gcs://mvn-public-tryformation/releases")

                // FIXME figure out url & gcs credentials using token & actor
                //     credentials()

            }
        }
    }
    publications.withType<MavenPublication> {
//                artifact(dokkaJar)
//                artifact(sourcesJar)
    }
}

