import org.gradle.api.tasks.SourceSetContainer

plugins {
    kotlin("jvm") version "2.3.20" apply false
}
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "application")
    repositories { mavenCentral() }
    dependencies {
        "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5:2.3.20")
        "testImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher:1.12.2")
    }
    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> { jvmToolchain(21) }
    extensions.configure<JavaApplication> { mainClass.set("course.MainKt") }
    tasks.withType<Test>().configureEach { useJUnitPlatform() }
    val mainSourceSet = extensions.getByType<SourceSetContainer>()["main"]
    (1..12).forEach { index ->
        val number = index.toString().padStart(2, '0')
        tasks.register<JavaExec>("task$number") {
            group = "learning"
            description = "Run coroutine exercise $number"
            classpath = mainSourceSet.runtimeClasspath
            mainClass.set("course.Task${number}Kt")
            standardInput = System.`in`
        }
    }
}
