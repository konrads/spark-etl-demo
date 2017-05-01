lazy val sparkEtl = RootProject(uri("https://github.com/konrads/spark-etl.git"))
lazy val root = Project("root", file("."))
  .dependsOn(sparkEtl)
  .settings(
    name := "spark-etl-demo",

    version := "0.0.1",

    scalaVersion := "2.11.8",

    scalacOptions ++= Seq("-deprecation", "-feature"),

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

lazy val validateConf = inputKey[Unit]("validate config")
lazy val spark_tools = (project in file("."))
  .settings(
    name := "spark_tools",

    scalaVersion := "2.11.8",

    // FIXME: hardcoded params in sbt only class: mainClass in Compile := Some("spark_etl.Main"),
    mainClass in Compile := Some("spark_etl.MainSbt"),

    validateConf := Def.taskDyn {
      // FIXME: should take params here: (run in Runtime).toTask(" -Denv.env=dev validate-local")
      (run in Runtime).toTask("")
    }.value
  )
  .dependsOn(sparkEtl)
