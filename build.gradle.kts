plugins {
    kotlin("jvm") version "1.4.10"
}

group = "net.transgene"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        manifest {
            attributes["Main-Class"] = "net.transgene.user_merge.MainKt"
        }
        from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}


tasks.test {
    useJUnitPlatform()
}