package com.wacai.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.github.dreamhead.moco.HttpServer;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.TreeMap;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:zhong.lunfu@gmail.com">zhongl<a>
 */
public class HttpReporterTest {

    @Test
    public void should_report_metrics() throws Exception {
        final HttpServer server = httpServer(12306, log());

        server.post(and(
                eq(jsonPath("$.meters.test.count"), "1"),
                eq(jsonPath("$.startTime"), ManagementFactory.getRuntimeMXBean().getStartTime() + ""),
                eq(header("Content-Type"), "application/json"))).response("OK");

        running(server, () -> {
            final MetricRegistry registry = new MetricRegistry();
            final URI uri = URI.create("http://localhost:12306/");
            final HttpReporter reporter = HttpReporter.forRegistry(registry, uri)
                                                      .callback(msgOrException -> assertThat(msgOrException, is("OK")))
                                                      .build();

            final Meter meter = mock(Meter.class);
            when(meter.getCount()).thenReturn(1L);
            when(meter.getMeanRate()).thenReturn(2.0);
            when(meter.getOneMinuteRate()).thenReturn(5.0);
            when(meter.getFiveMinuteRate()).thenReturn(4.0);
            when(meter.getFifteenMinuteRate()).thenReturn(3.0);

            final TreeMap<String, Meter> meters = new TreeMap<>();
            meters.put("test", meter);
            reporter.report(null, null, null, meters, null);

        });
    }
}