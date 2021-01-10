import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.4"
ThisBuild / autoCompilerPlugins := true
ThisBuild / scalacOptions ++= Seq(
  "-Wconf:any:error", // for scalac warnings
  "-Xfatal-warnings", // for wartremover warts
  "-explaintypes", // Explain type errors in more detail.
  "-language:higherKinds" // Allow higher-kinded types
)

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    name := "TaglessFinal101",
    mainClass in (Compile, run) := Some("tf.Main"),
    mainClass in (Compile, packageBin) := Some("tf.Main"),
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.1",
    libraryDependencies += compilerPlugin(
      "org.augustjune" %% "context-applied" % "0.1.4"
    ),
    libraryDependencies += compilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full
    ),
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.1",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "2.3.1"
    //wartremoverErrors ++= Warts.allBut(Wart.OptionPartial)
  )
