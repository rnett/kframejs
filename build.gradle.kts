plugins {
    id("org.jetbrains.kotlin.js") version "1.3.60"
    `maven-publish`
    id("kotlinx-serialization") version "1.3.60"
}

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.13.0")
}

kotlin.target.browser { }

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    classifier = "sources"
    from(kotlin.sourceSets["main"].kotlin.srcDirs)
}

publishing {
    publications {
        create("default", MavenPublication::class) {
            from(components["kotlin"])
            group = "com.rnett.kframe"
            artifactId = "kframe"
            version = "1.0.0"

            artifact(sourcesJar)
        }
    }
}