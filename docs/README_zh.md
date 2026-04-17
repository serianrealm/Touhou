# 代码仓库

<p align="center">
  <strong>语言: <a href="../README.md">English</a> | <a href="README_zh.md">中文</a></strong>
</p>

这个仓库是我的《软件构造》课程作业。项目使用 Java 开发，并通过 Gradle 构建。

## 环境要求

- JDK 25
- 仓库中已包含 Gradle Wrapper

## 构建

在仓库根目录运行：

```bash
./gradlew build
```

## 测试

运行全部测试：

```bash
./gradlew test
```

## 运行

启动项目：

```bash
./gradlew :apps:run
```

## UML

- PlantUML 源文件位于 `docs/uml/`
- 兼容用途的 PNG 备份目录位于 `docs/uml_png/`

## 项目结构

```text
.
├── apps/          # 应用源码与测试
├── docs/          # 项目文档
│   ├── uml/       # PlantUML 文件
│   └── uml_png/   # 兼容用途的 PNG 备份目录
├── gradle/        # Gradle Wrapper 文件
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## 说明

本项目仅用于课程学习与作业提交。
