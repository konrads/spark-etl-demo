// Enables running with spark dependencies, as per:
// http://apache-spark-user-list.1001560.n3.nabble.com/libraryDependencies-configuration-is-different-for-sbt-assembly-vs-sbt-run-td565.html

import dependencies.deps
import sbtassembly.AssemblyKeys._
import sbt._
import Keys._

object ApplicationBuild extends Build {

  val Unprovided = config("unprovided") extend Runtime

  val root = project.in(file(".")).
    configs(Unprovided).
    settings(
      libraryDependencies ++= deps.map {
        case d if d.configurations == Some("provided") => d.copy(configurations=Some("unprovided"))
        case other => other
      },
      assembleArtifact in assemblyPackageScala := false
    ).
    settings(inConfig(Unprovided)(Classpaths.configSettings ++ Seq(
      run <<= Defaults.runTask(fullClasspath, mainClass in(Runtime, run), runner in(Runtime, run))
    )): _*).
    settings(
      run <<= (run in Unprovided)
    )
}