#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.39.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.googlegithubactions.AuthV1
import it.krzeminski.githubactions.actions.googlegithubactions.SetupGcloudV1
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.writeToFile

val workflow = workflow(
    name = "Deploy to maven",
    on = listOf(
        Push(branches = listOf("main"))
    ),
    sourceFile = __FILE__.toPath(),
) {
    val publishJob = job(
        id = "publish",
        runsOn = RunnerType.UbuntuLatest
    ) {
        uses(
            name = "checkout",
            action = CheckoutV3()
        )
        uses(
            name = "setup jdk",
            action = SetupJavaV3(
                javaPackage = SetupJavaV3.JavaPackage.Jdk,
                javaVersion = "17",
                architecture = "x64",
                distribution = SetupJavaV3.Distribution.Adopt
            )
        )
        uses(
            name = "gcloud auth",
            action = AuthV1(
                credentialsJson = expr("secrets.GOOGLE_CLOUD_KEY"),
            )
        )
        uses(
            name = "setup cloud sdk",
            action = SetupGcloudV1()
        )
        uses(
            name = "test & publish library package",
            action = GradleBuildActionV2(
                arguments = "clean build publish --scan",
            )
        )
//        uses(
//            name = "slack notification",
//            action = ActionSlackV3(
//                status = ActionSlackV3.Status.Custom(expr("job.status")),
//                fields = listOf(
//                    "repo", "message", "author", "eventName", "ref", "workflow", "took"
//                ),
//                authorName = "api-client published"
//            ),
//            env = linkedMapOf(
//                "SLACK_WEBHOOK_URL" to expr("secrets.SLACK_HOOK")
//            )
//        )
    }
}
workflow.writeToFile(addConsistencyCheck = true)
