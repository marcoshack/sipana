package br.com.voicetechnology.sipana.statistic;

import br.com.voicetechnology.sipana.SipanaCapturer;

public class StatisticSipResponse {
    private static final String DEFAULT_SAMPLE_MAX = "500";

    private final int SAMPLE_MAX;

    private int status_code;

    private long[] response_times;

    private int response_counter;

    private int response_counter_total;

    private long response_total;

    private long response_max;

    private long response_min;

    public StatisticSipResponse(int code) {
        status_code = code;
        response_counter = response_counter_total = 0;
        response_min = -1;
        response_max = 0;

        // Set sample space of responses
        SAMPLE_MAX = Integer.parseInt(SipanaCapturer.getProperties()
                .getProperty("sipana.statistic.sample", DEFAULT_SAMPLE_MAX));
        response_times = new long[SAMPLE_MAX];
    }

    public void addResponseTime(long time) {
        if (response_counter == (SAMPLE_MAX - 1)) {
            response_counter = 0;
        }

        if (time > response_max) {
            response_max = time;
        }

        // POG
        if (response_min == -1) {
            response_min = time;
        }

        if (time < response_min) {
            response_min = time;
        }

        response_times[response_counter] = time;
        response_total += time;
        response_counter_total++;
        response_counter++;
    }

    public long getMaxTime() {
        return response_max;
    }

    public long getMinTime() {
        return response_min;
    }

    /*
     * Arithmetic mean of "sipana.statistic.sample" nonzero response times.
     * 
     * TODO: Returned a negative value for a 200 OK response time of a Register
     * request.
     */
    public long getSampleAvgTime() {
        long sum = 0;
        int i, nonzero;

        for (i = 0, nonzero = 0; i <= (SAMPLE_MAX - 1); i++) {
            sum += response_times[i];
            if (response_times[i] > 0) {
                nonzero++;
            }
        }

        if (sum > 0) {
            return (sum / nonzero);
        } else {
            return 0;
        }
    }

    /*
     * Arithmetic mean of all response times.
     */
    public long getTotalAvgTime() {
        return (response_total / response_counter_total);
    }

    public int getStatusCode() {
        return status_code;
    }

    public long getCounter() {
        return response_counter_total;
    }

}
