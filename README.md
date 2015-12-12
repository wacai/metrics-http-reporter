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

## Metric Message Format

```json
{
  "timestamp":1449918125458,
  "meters":
    {
      "test":
        {
          "count":1,
          "m15_rate":3.0,
          "m1_rate":5.0,
          "m5_rate":4.0,
          "mean_rate":2.0,
          "units":"events/second"
        }
    },
  "startTime":1449918124182
}
```

## Change Log

### 0.0.3

* Add timestamp.

### 0.0.2

* Remove dependency of slf4j;
* Json message ignore null field.

### 0.0.1

* POST metrics message to a dashboard web server.