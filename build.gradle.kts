plugins {
    id("java")
}

group = "io.github.hunterherbst"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes (
                "Main-Class" to "io.github.hunterherbst.Application"
        )
    }
}