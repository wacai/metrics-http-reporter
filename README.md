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

### Example

<http://git.caimi-inc.com/jushi/perfmock/blob/master/src/main/java/com/wacai/perfmock/Main.java>

## Metric Message Protocol

```json
{
  "timestamp": 1450081977900,
  "gauges": {
    "accept.throttle": {
      "value": 0
    },
    "file.desc.used": {
      "value": 0.013671875
    },
    "memory.used": {
      "value": 0.03368591119063872
    }
  },
  "counters": {
    "accept.failure": {
      "count": 0
    },
    "kafka.failure": {
      "count": 0
    },
    "qq.failure": {
      "count": 0
    }
  },
  "histograms": {},
  "meters": {
    "accept": {
      "count": 1,
      "m15_rate": 0.0011080303990206543,
      "m1_rate": 0.015991117074135343,
      "m5_rate": 0.0033057092356765017,
      "mean_rate": 0.07386184983014359,
      "units": "events/second"
    },
    "kafka": {
      "count": 1,
      "m15_rate": 0,
      "m1_rate": 0,
      "m5_rate": 0,
      "mean_rate": 0.06668292396352898,
      "units": "events/second"
    },
    "qq": {
      "count": 1,
      "m15_rate": 0.0011080303990206543,
      "m1_rate": 0.015991117074135343,
      "m5_rate": 0.0033057092356765017,
      "mean_rate": 0.07386377024171993,
      "units": "events/second"
    }
  },
  "timers": {},
  "startTime": 1450081962395
}
```

1. No timers and histograms;
1. Every Meter **MUST** have an failure Counter with it.

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