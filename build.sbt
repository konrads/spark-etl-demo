
lazy val genDot = taskKey[Unit]("generate dot")
lazy val validateConf = taskKey[Unit]("validate config")

lazy val sparkEtl = RootProject(uri("https://github.com/konrads/spark-etl.git"))
lazy val root = Project("root", file("."))
  .dependsOn(sparkEtl)
  .settings(
    name := "spark-etl-demo",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    mainClass in Compile := Some("spark_etl.Main"),
    genDot := Def.taskDyn {
      (run in Runtime).toTask(" -Denv.path=__path__ --lineage-file=src/main/lineage/lineage.dot lineage-dot")
    }.value,
    validateConf := Def.taskDyn {
      (run in Runtime).toTask(" -Denv.path=__path__ validate-local")
    }.value,
    test in Test <<= (test in Test) dependsOn validateConf,
    libraryDependencies ++= {
      lazy val sparkVsn     = "2.0.1"
      lazy val scalazVsn    = "7.2.7"
      Seq(
        "org.apache.spark" %% "spark-core"        % sparkVsn     % "compile",
        "org.apache.spark" %% "spark-sql"         % sparkVsn     % "compile",
        "org.scalaz"       %% "scalaz-core"       % scalazVsn,
        "net.jcazevedo"    %% "moultingyaml"      % "0.4.0",
        "org.rogach"       %% "scallop"           % "2.0.2"
      )
    }
  )
