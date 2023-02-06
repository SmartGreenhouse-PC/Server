group = "it.unibo.smartgh"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "it.unibo.smartgh.soilMoisture.SoilMoistureServiceLauncher"
    }
}


