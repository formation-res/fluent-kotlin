/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/7.5/userguide/multi_project_builds.html
 * This project uses @Incubating APIs which are subject to change.
 */

rootProject.name = "fluent-kotlin"

plugins {
    id("com.gradle.enterprise") version "3.13.1"
    id("de.fayard.refreshVersions") version "0.60.3"
}

refreshVersions {
    extraArtifactVersionKeyRules(file("version_key_rules.txt"))
}

// https://dev.to/jmfayard/the-one-gradle-trick-that-supersedes-all-the-others-5bpg
gradleEnterprise {
    buildScan {
        // uncomment this to scan every gradle task
//        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        buildScanPublished {
            file("buildscan.log").appendText("${java.util.Date()} - $buildScanUri\n")
        }
    }
}