buildscript {

    @Suppress("unused")
    val versions: Map<String, String> by extra {
        mapOf("appCompat" to "1.0.0",
                "constraintLayout" to "2.0.0-alpha2",
                "coroutines" to "1.0.0",
                "espressoRunner" to "1.1.0",
                "espressoCore" to "3.1.0",
                "junit" to "4.12")
    }

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0-alpha02")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.0")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}