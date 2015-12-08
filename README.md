## Install

```xml

<dependency>
    <groupId>com.wacai</groupId>
    <artifactId>metrics-http-reporter</artifactId>
    <version>${version}</version>
</dependency>

```

## Usage

```java
MetricRegistry registry = new MetricRegistry(); 
URI uri = URI.create("http://example.com/metrics")
HttpReporter reporter = HttpReporter.forRegistry(registry, uri).build();
report.start(1, TimeUnit.SECONDS);

...

report.stop();
```

