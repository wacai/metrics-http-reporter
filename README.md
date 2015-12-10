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

## Change Log

### 0.0.3

* Add timestamp.

### 0.0.2

* Remove dependency of slf4j;
* Json message ignore null field.

### 0.0.1

* POST metrics message to a dashboard web server.