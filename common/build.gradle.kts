group = "it.unibo.smartgh"
version = "0.1.0-archeo+5f38f72"

repositories {
    mavenCentral()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

