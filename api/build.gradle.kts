import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "me.toelke.pnpmusicapp"
version = System.getenv().getOrDefault("APP_VERSION", "0.1.0")
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val mockkVersion = "1.12.3"
val kotestVersion = "5.1.0"

dependencies {
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("com.mpatric:mp3agic:0.9.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		setEvents(listOf("failed"))
		setExceptionFormat("full")
		showCauses = true
		showStackTraces = true
	}
}
