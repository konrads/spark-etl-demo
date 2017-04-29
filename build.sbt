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
      lazy val scalaTestVsn = "3.0.0"
      Seq(
        // core
        "org.apache.spark" %% "spark-core"        % sparkVsn     % "compile",
        "org.apache.spark" %% "spark-sql"         % sparkVsn     % "compile",
        "org.scalaz"       %% "scalaz-core"       % scalazVsn,
        "net.jcazevedo"    %% "moultingyaml"      % "0.4.0",
        "org.rogach"       %% "scallop"           % "2.0.2",

        // testing
        "org.scalatest"    %% "scalatest"         % scalaTestVsn % "test",
        "org.scalacheck"   %% "scalacheck"        % "1.13.4"     % "test"
      )
    }
  )

lazy val validateConf = inputKey[Unit]("validate config")
lazy val spark_tools = (project in file("."))
  .settings(
    name := "spark_tools",

    scalaVersion := "2.11.8",

    mainClass in Compile := Some("spark_etl.MainSbt"),
    
    validateConf := Def.taskDyn {
      (run in Runtime).toTask("")
    }.value
  )
  .dependsOn(sparkEtl)
