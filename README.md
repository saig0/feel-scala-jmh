# FEEL-Scala JMH-Benchmark

This project contains [JMH](https://github.com/openjdk/jmh) benchmarks for
the [FEEL-Scala](https://github.com/camunda/feel-scala) project.

## Usage

1. Build the benchmark

```
mvn clean verify
```

2. Run the benchmark

```
java -jar target/benchmarks.jar
```

With JSON output format and result file:

```
java -jar target/benchmarks.jar -rf json -o benchmark-results.txt
```
