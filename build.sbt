import dependencies.deps

lazy val validateConf = taskKey[Unit]("validate config")
lazy val genDot = taskKey[Unit]("generate dot")
lazy val stripPrefixes = taskKey[Unit]("strip prefixes")
name := "spark-etl-demo"
scalaVersion := "2.11.8"
mainClass in Compile := Some("spark_etl.CLI")
scalacOptions ++= Seq("-deprecation", "-feature")
testOptions in Test += Tests.Argument("-oF")
libraryDependencies ++= deps
test in assembly := {}
validateConf := Def.taskDyn {
  (run in Compile).toTask(" -Denv.path=__path__ validate-local")
}.value
genDot := Def.taskDyn {
  (run in Compile).toTask(" -Denv.path=__path__ --lineage-file=src/main/lineage/lineage.dot lineage-dot")
}.value
stripPrefixes := Def.taskDyn {
  (run in Compile).toTask(" -Denv.path=__path__ strip-prefixes")
}.value
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))
test in Test <<= (test in Test) dependsOn validateConf

// META-INF discarding
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
