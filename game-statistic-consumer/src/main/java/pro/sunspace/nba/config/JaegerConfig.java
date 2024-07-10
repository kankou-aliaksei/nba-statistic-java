package pro.sunspace.nba.config;

import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import io.jaegertracing.Configuration;

@org.springframework.context.annotation.Configuration
public class JaegerConfig {

    @Value("${jaeger.service.name}")
    private String serviceName;

    @Value("${jaeger.endpoint}")
    private String jaegerEndpoint;

    @Bean
    public Tracer jaegerTracer() {
        Configuration.SamplerConfiguration samplerConfig = io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.ReporterConfiguration reporterConfig = io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true)
                .withSender(io.jaegertracing.Configuration.SenderConfiguration.fromEnv().withEndpoint(jaegerEndpoint));

        return new Configuration(serviceName)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig)
                .getTracer();
    }
}
