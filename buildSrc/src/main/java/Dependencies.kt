import org.gradle.api.JavaVersion

object Config {
    const val targetSdkVersion = 33
    const val compileSdkVersion = targetSdkVersion
    const val minSdkVersion = 23
    const val buildToolsVersion = "30.0.3"
    const val applicationId = "com.androidapp.movieappmvvm"
    val javaVersion = JavaVersion.VERSION_1_8
}

object Version {
    const val kotlinCoroutine = "1.4.2"
    const val room = "2.2.6"
    const val work = "2.5.0"
    const val dagger = "2.44"
    const val retrofit2 = "2.9.0"
    const val glide = "4.11.0"
    const val navigation = "2.5.3"
}


object Room {
    const val runtime = "androidx.room:room-runtime:${Version.room}"
    const val compiler = "androidx.room:room-compiler:${Version.room}"
    const val ktx = "androidx.room:room-ktx:${Version.room}"
}

object Work {
    const val runtime = "androidx.work:work-runtime:${Version.work}"
    const val ktx = "androidx.work:work-runtime-ktx:${Version.work}"
}

object Dagger {
    const val dagger = "com.google.dagger:dagger:${Version.dagger}"
    const val compiler = "com.google.dagger:dagger-compiler:${Version.dagger}"
}

object Coroutine {
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlinCoroutine}"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinCoroutine}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit2}"
    const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Version.retrofit2}"
    const val coroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.0"
}

object ViewModel {
    const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
}

object Glide {
    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
    const val compiler = "com.github.bumptech.glide:compiler:${Version.glide}"
}

object Navigation {
    const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
}