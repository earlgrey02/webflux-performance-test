plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    group = "com.example"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }

    tasks {
        compileKotlin {
            compilerOptions {
                freeCompilerArgs.addAll("-Xjsr305=strict")
            }
        }
    }
}
