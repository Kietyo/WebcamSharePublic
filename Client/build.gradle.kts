import com.soywiz.korge.gradle.*

val ktor_version: String by project

buildscript {
	val korgePluginVersion: String by project

	repositories {
		mavenLocal()
		mavenCentral()
		google()
		maven { url = uri("https://plugins.gradle.org/m2/") }
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
	}
}

apply<KorgeGradlePlugin>()

korge {
	id = "com.example.example"
// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets

	targetJvm()
//	targetJs()
//	targetDesktop()
//	targetIos()
//	targetAndroidIndirect() // targetAndroidDirect()

	project.korge {
		addDependency("jvmMainImplementation", "io.ktor:ktor-client-core:$ktor_version")
		addDependency("jvmMainImplementation", "io.ktor:ktor-client-cio:$ktor_version")
		addDependency("jvmMainImplementation", "io.ktor:ktor-client-websockets:$ktor_version")
	}
}