name := "TaglessFinal101"

version := "0.1"

scalaVersion := "2.13.4"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.1"

autoCompilerPlugins := true

libraryDependencies += compilerPlugin("org.augustjune" %% "context-applied" % "0.1.4")

libraryDependencies += compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full)
