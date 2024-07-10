package pro.sunspace.nba.config;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class JaegerConfig {

    @Value("${jaeger.service.name}")
    private String serviceName;

    @Value("${jaeger.endpoint}")
    private String jaegerEndpoint;

    @Bean
    public Tracer tracer() {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true)
                .withSender(Configuration.SenderConfiguration.fromEnv().withEndpoint(jaegerEndpoint));

        return new Configuration(serviceName)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig)
                .getTracer();
    }
}
