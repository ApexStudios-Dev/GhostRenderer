# GhostRenderer (Beta)

GhostRenderer is a ghost rendering library for Minecraft (NeoForge), allowing you to render ghosts in-level with ease.

Currently, this library allows rendering ghost blocks, but there are plans to expand this to entities in the future, as well as allowing for customisation over how ghosts are rendered.

<details>
<summary> Groovy DSL (build.gradle) </summary>

```groovy
repositories {
    maven { url "https://maven.apexmodder.com/releases" }
}

dependencies {
    implementation "dev.apexstudios:ghostrenderer:<version>"
}
```

</details>

<details>
<summary> Kotlin DSL (build.gradle.kts) </summary>

```kotlin
repositories {
    maven("https://maven.apexmodder.com/releases")
}

dependencies {
    implementation("dev.apexstudios:ghostrenderer:<version>")
}
```

</details>