group = "it.unibo.smartgh"
version = "0.1.0"

repositories {
    mavenCentral()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.smartgh.greenhouseCommunication.GreenhouseCommunicationServiceLauncher"
    }
}


