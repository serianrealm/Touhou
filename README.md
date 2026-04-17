# Workspace Layout

Running tree .
```text
.
├── apps/
│   ├── build.gradle
│   └── src/
│       ├── main/java/...
│       └── test/java/...
├── build-logic/
│   ├── build.gradle
│   └── src/main/groovy/
│       ├── com.example.java-application-conventions.gradle
│       ├── com.example.java-common-conventions.gradle
│       └── com.example.java-library-conventions.gradle
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── libraries/
│   ├── domain/
│   │   └── commons/
│   │       ├── build.gradle
│   │       └── src/
│   │           ├── main/java/...
│   │           └── test/java/...
│   └── platform/
│       ├── bootstrap/
│       │   ├── build.gradle
│       │   └── src/
│       │       ├── main/java/...
│       │       └── test/java/...
│       └── data-access/
│           ├── build.gradle
│           └── src/
│               ├── main/java/...
│               └── test/java/...
├── build.gradle
├── gradle.properties
├── settings.gradle
├── gradlew
└── gradlew.bat

112 directories, 262 files
```

# How to work with this workspace

Run everything from the repository root:

```bash
./gradlew build
./gradlew test
./gradlew :apps:run
```

Run individual modules when needed:

```bash
./gradlew :libraries:domain:commons:test
./gradlew :libraries:platform:data-access:build
./gradlew :libraries:platform:bootstrap:test
```

# Trouble Shooting
## X11 not Found
`java.lang.UnsatisfiedLinkError: ... libawt_xawt.so: libX11.so.6: cannot open shared object file`

Run
```sh
sudo apt update
sudo apt install -y libx11-6 libxext6 libxrender1 libxtst6 libxi6 libxrandr2 libxcursor1 libxinerama1 libfontconfig1
```

## UML Display Error
Run
```sh
sudo apt update
sudo apt install graphviz
```

# NOTE
This work supports only for my software construction class only. Built with gradle.