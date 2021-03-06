spark-etl-demo
==============

Demo application for [spark-etl](https://github.com/konrads/spark-etl) library.

Build status (master): [![Build Status](https://travis-ci.org/konrads/spark-etl-demo.svg?branch=master)](https://travis-ci.org/konrads/spark-etl-demo)

Suggested ba-dev-ops interactions
---------------------------------

### BA:
BA works off the [sql directory in BA branch](../../tree/BA), populating only [app.yaml config](../../tree/BA/src/main/resources/app.yaml) and [resource SQLs](../../tree/BA/src/main/resources). When done, BA notifies the developer.

### Dev:
Developer merges [BA branch](../../tree/BA) into `master`, potentially using utilities that massage SQL for production (eg. via `sbt stripPrefixes`). Developer initiates the `build`, ensuring `sbt test` target passes, as that validates config/SQL, as well as any code tests. This sbt target should also be run within CI, see [.travis.yml](.travis.yml).
The code can then build and published on Spark cluster environment.

This project utilizes library [spark-etl](https://github.com/konrads/spark-etl), please look there for more information.

*Note*: to reduce the size of fatjar, unused dependencies (joda-time & co) have been identified and pruned thanks to `net.virtual-void:sbt-dependency-graph` plugin:
```
sbt dependencyTree
```

### Ops:
Once the releasable artefacts are deployed to Spark cluster environment, run with [run.sh](src/main/resources/run.sh):
```
> ./run.sh
  Usage:
    help
    validate-local
    validate-remote
    transform-load
    extract-check
    transform-check
```

*Always* start by running validations:
```
> # check config and SQL
> ./run.sh -Denv.path=<root> validate-local

> # check hdfs paths, other remote connectivity
> ./run.sh -Denv.path=<root> validate-remote
```

To run transform and persist results:
```
> ./run.sh -Denv.path=<root> transform-load
```

To fetch yarn logs after the job is run, set `PACKAGE_LOGS=true`. *Note: not for production!*
```
> export PACKAGE_LOGS=true
> ./run.sh -Denv.path=<root> transform-load

> # list zipped up logs (from driver and from the cluster)
> ls logs/current
logs_application_XXXXXXXXXXXXX_YYYYYY.zip

> cd logs/current
> unzip logs_application_XXXXXXXXXXXXX_YYYYYY.zip
Archive:  logs_application_XXXXXXXXXXXXX_YYYYYY.zip
  inflating: application_XXXXXXXXXXXXX_YYYYYY.local.log
  inflating: application_XXXXXXXXXXXXX_YYYYYY.remote.log
```

Lineage
-------
Following lineage/dependency graph was generated via:
```
sbt genDot
# after graphviz install, eg. brew install graphviz
dot -Tgif src/main/lineage/lineage.dot -o src/main/lineage/lineage.gif
```
![dot-lineage](src/main/lineage/lineage.gif)

##### Legend
* <sub>ellipse = extract</sub>
* <sub>component = transform</sub>
* <sub>cylinder = load</sub>
* <sub>full line = relationship configured explicitly</sub>
* <sub>dotted line = relationship derived from SQL</sub>
