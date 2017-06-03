import sbt._

object dependencies {
  val sparkVsn  = "2.0.1"
  val scalazVsn = "7.2.7"

  val deps =
    Seq(
    "org.apache.spark" %% "spark-core"        % sparkVsn     % "provided",
    "org.apache.spark" %% "spark-sql"         % sparkVsn     % "provided",
    "org.scalaz"       %% "scalaz-core"       % scalazVsn,
    "net.jcazevedo"    %% "moultingyaml"      % "0.4.0",
    "org.rogach"       %% "scallop"           % "2.0.2"
  )
}

