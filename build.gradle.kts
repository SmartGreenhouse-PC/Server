plugins {
    java
    jacoco
    id("com.github.johnrengelman.shadow") version "5.2.0"
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.javadocAggregate)
}

group = "it.unibo.smartgh"
version = "1.0-SNAPSHOT"

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

repositories {
    mavenCentral()
}

allprojects{
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "com.github.johnrengelman.shadow")

    jacoco {
        toolVersion = "0.8.8"
        reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
    }

    dependencies {
        implementation(rootProject.libs.bundles.vertx.dependencies)
        implementation(rootProject.libs.mongodb.driver)
        implementation(rootProject.libs.gson)
        implementation(rootProject.libs.jacoco)
        testImplementation(rootProject.libs.junit.api)
        testRuntimeOnly(rootProject.libs.junit.engine)
    }

    tasks.jacocoTestReport {
        reports {
            xml.required.set(false)
            csv.required.set(false)
            html.required.set(true)
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jacocoAggregatedReport by tasks.creating(JacocoReport::class) {
    var classDirs: FileCollection = files()
    subprojects.forEach {
        dependsOn(it.tasks.test)
        dependsOn(it.tasks.jacocoTestReport)
        sourceSets(it.sourceSets.main.get())
        classDirs += files(it.tasks.jacocoTestReport.get().classDirectories)
    }
    classDirectories.setFrom(classDirs)
    executionData.setFrom(
        fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec")
    )
    reports {
        xml.required.set(false)
        html.required.set(true)
        csv.required.set(false)
    }
}
