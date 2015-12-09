package com.wacai.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.config.metro.dev.FeatureReader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HttpReporter extends ScheduledReporter {

    private final URI              uri;
    private final int              timeoutMillis;
    private final Consumer<Object> callback;
    private final ObjectMapper     mapper;

    public static Builder forRegistry(MetricRegistry registry, URI uri) {
        return new Builder(registry, uri);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {

        try {
            final MetricMessage metricMessage = new MetricMessage(gauges, counters, histograms, meters, timers);
            final HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            post(metricMessage, conn);
            callback.accept(conn.getResponseMessage());
        } catch (IOException e) {
            callback.accept(e);
        }

    }

    private HttpReporter(MetricRegistry registry,
                         URI uri,
                         int timeoutMillis,
                         boolean showSamples, TimeUnit rateUnit,
                         TimeUnit durationUnit,
                         MetricFilter filter,
                         Consumer<Object> callback) {
        super(registry, "http-post-reporter", filter, rateUnit, durationUnit);
        this.uri = uri;
        this.timeoutMillis = timeoutMillis;
        this.callback = callback;
        final MetricsModule module = new MetricsModule(rateUnit, durationUnit, showSamples, filter);
        mapper = new ObjectMapper().registerModule(module);
    }

    private int post(MetricMessage metricMessage, HttpURLConnection conn) throws IOException {
        try {
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(timeoutMillis);
            conn.setReadTimeout(timeoutMillis);
            conn.setRequestProperty("Content-Type", "application/json");
            mapper.writeValue(conn.getOutputStream(), metricMessage);
            conn.getOutputStream().close();
            return conn.getResponseCode();
        } finally {
            conn.disconnect();
        }
    }

    public static class Builder {
        private final MetricRegistry   registry;
        private final URI              uri;
        private       int              timeoutMillis;
        private       MetricFilter     filter;
        private       TimeUnit         rateUnit;
        private       TimeUnit         durationUnit;
        private       boolean          showSamples;
        private       Consumer<Object> callback;

        private Builder(MetricRegistry registry, URI uri) {
            this.registry = registry;
            this.uri = uri;
            this.timeoutMillis = 1000;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.showSamples = false;
            this.filter = MetricFilter.ALL;
            this.callback = responseMessageOrException -> {};
        }

        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder timeoutMillis(int value) {
            this.timeoutMillis = value;
            return this;
        }

        public Builder showSamples(boolean value) {
            this.showSamples = value;
            return this;
        }

        public Builder callback(Consumer<Object> value) {
            this.callback = value;
            return this;
        }


        public HttpReporter build() {
            return new HttpReporter(registry, uri, timeoutMillis, showSamples, rateUnit, durationUnit, filter, callback);
        }
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class MetricMessage {
        private final SortedMap<String, Gauge>     gauges;
        private final SortedMap<String, Counter>   counters;
        private final SortedMap<String, Histogram> histograms;
        private final SortedMap<String, Meter>     meters;
        private final SortedMap<String, Timer>     timers;

        public MetricMessage(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
                             SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
            this.gauges = gauges;
            this.counters = counters;
            this.histograms = histograms;
            this.meters = meters;
            this.timers = timers;
        }

        public SortedMap<String, Gauge> getGauges() {
            return gauges;
        }

        public SortedMap<String, Counter> getCounters() {
            return counters;
        }

        public SortedMap<String, Histogram> getHistograms() {
            return histograms;
        }

        public SortedMap<String, Meter> getMeters() {
            return meters;
        }

        public SortedMap<String, Timer> getTimers() {
            return timers;
        }
    }
}
