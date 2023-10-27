#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.4.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile


val workflow = workflow(
    name = "Deploy to maven",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(branches = listOf("main"))
    ),
    sourceFile = __FILE__.toPath(),
) {
    val publishJob = job(
        id = "publish",
        runsOn = RunnerType.UbuntuLatest
    ) {
        uses(
            name = "checkout",
            action = CheckoutV4()
        )
        uses(
            name = "setup jdk",
            action = SetupJavaV3(
                javaPackage = SetupJavaV3.JavaPackage.Jdk,
                javaVersion = "17",
                architecture = "x64",
                distribution = SetupJavaV3.Distribution.Adopt,
                cache = SetupJavaV3.BuildPlatform.Gradle
            )
        )
//        uses(
//            name = "gcloud auth",
//            action = AuthV1(
//                credentialsJson = expr("secrets.GOOGLE_CLOUD_KEY"),
//            )
//        )
//        uses(
//            name = "setup cloud sdk",
//            action = SetupGcloudV1()
//        )
        uses(
            name = "test",
            action = GradleBuildActionV2(
                arguments = "clean build --scan",
            )
        )
    }
}
workflow.writeToFile(addConsistencyCheck = true)
