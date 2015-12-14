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

## Metric Message Protocol

```json
{
  "timestamp": 1450078773653,
  "gauges": {
    "accept.throttle": {
      "value": 0
    },
    "file.desc.used": {
      "value": 0.00205078125
    },
    "memory.used": {
      "value": 0.03368591119063872
    }
  },
  "counters": {
    "accept.success": {
      "count": 0
    }
  },
  "histograms": {},
  "meters": {
    "accept": {
      "count": 0,
      "m15_rate": 0,
      "m1_rate": 0,
      "m5_rate": 0,
      "mean_rate": 0,
      "units": "events/second"
    }
  },
  "timers": {},
  "startTime": 1450078768148
}
```

1. No timers and histograms;
1. Every Meter **MUST** have an success Counter with it.

## Change Log

### 0.0.4

* Add jvm start time.

### 0.0.3

* Add timestamp.

### 0.0.2

* Remove dependency of slf4j;
* Json message ignore null field.

### 0.0.1

* POST metrics message to a dashboard web server.