package ar.com.odontomoron.config;

import ar.com.fluxit.verduleria.config.CORSFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import ar.com.odontomoron.web.filter.CachingHttpHeadersFilter;
import ar.com.odontomoron.web.filter.StaticResourcesProductionFilter;
import ar.com.odontomoron.web.filter.gzip.GZipServletFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initCorsFilter(servletContext, disps); 
        if (!env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
            initMetrics(servletContext, disps);
        }
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            initCachingHttpHeadersFilter(servletContext, disps);
            initStaticResourcesProductionFilter(servletContext, disps);
            initGzipFilter(servletContext, disps);
        }
        log.info("Web application fully configured");
    }

    /**
     * Set up Mime types.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }

    /**
     * Initializes the GZip filter.
     */
    private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering GZip Filter");
        FilterRegistration.Dynamic compressingFilter = servletContext.addFilter("gzipFilter", new GZipServletFilter());
        Map<String, String> parameters = new HashMap<>();
        compressingFilter.setInitParameters(parameters);
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.svg");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.ttf");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/api/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
        compressingFilter.setAsyncSupported(true);
    }

    /**
     * Initializes the static resources production Filter.
     */
    private void initStaticResourcesProductionFilter(ServletContext servletContext,
                                                     EnumSet<DispatcherType> disps) {

        log.debug("Registering static resources production Filter");
        FilterRegistration.Dynamic staticResourcesProductionFilter =
                servletContext.addFilter("staticResourcesProductionFilter",
                        new StaticResourcesProductionFilter());

        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
        staticResourcesProductionFilter.setAsyncSupported(true);
    }

    /**
     * Initializes the cachig HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter(env));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * Initializes Metrics.
     */
    private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Initializing Metrics registries");
        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
                metricRegistry);
        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
                metricRegistry);

        log.debug("Registering Metrics Filter");
        FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
                new InstrumentedFilter());

        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
        metricsFilter.setAsyncSupported(true);

        log.debug("Registering Metrics Servlet");
        ServletRegistration.Dynamic metricsAdminServlet =
                servletContext.addServlet("metricsServlet", new MetricsServlet());

        metricsAdminServlet.addMapping("/metrics/metrics/*");
        metricsAdminServlet.setAsyncSupported(true);
        metricsAdminServlet.setLoadOnStartup(2);
    }
    
    /**
    * Initializes the CORS Filter.
    */
    private void initCorsFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) 
    {
	    log.debug("Registering CORS Filter");
	    FilterRegistration.Dynamic corsFilter = servletContext.addFilter(
	    "corsFilter", new CORSFilter());
	    final String allowedHttpHeaders = CORSFilter.DEFAULT_ALLOWED_HTTP_HEADERS + ",access-control-allow-credentials, "
                    + "x-auth-token, Access-Control-Allow-Origin, X-CSRFToken, Cookie";
	    final String allowedHttpMethods = CORSFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
	    final String allowedOrigins = CORSFilter.DEFAULT_ALLOWED_ORIGINS;
	    final String exposedHeaders = CORSFilter.DEFAULT_EXPOSED_HEADERS;
	    final String supportCredentials = "true";
	    final String preflightMaxAge = CORSFilter.DEFAULT_PREFLIGHT_MAXAGE;
	    final String loggingEnabled = CORSFilter.DEFAULT_LOGGING_ENABLED;
	    final String decorateRequest = CORSFilter.DEFAULT_DECORATE_REQUEST;
	
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_ALLOWED_HEADERS,
	    allowedHttpHeaders);
	
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_ALLOWED_METHODS,
	    allowedHttpMethods);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_ALLOWED_ORIGINS,
	    allowedOrigins);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_EXPOSED_HEADERS,
	    exposedHeaders);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_SUPPORT_CREDENTIALS,
	    supportCredentials);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_PREFLIGHT_MAXAGE,
	    preflightMaxAge);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_LOGGING_ENABLED,
	    loggingEnabled);
	    corsFilter.setInitParameter(CORSFilter.PARAM_CORS_REQUEST_DECORATE,
	
	    decorateRequest);
	
	    corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
