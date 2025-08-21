package com.du.besttrip.ordersb2c.config.properties;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;

import java.util.*;
import java.util.stream.Collectors;


public class ApplicationClientProperties extends FeignClientProperties {

    private boolean defaultToProperties = true;

    private String defaultConfig = "default";

    private Map<String, ApplicationClientConfiguration> clients = new HashMap<>();

    /**
     * Feign clients do not encode slash `/` characters by default. To change this
     * behavior, set the `decodeSlash` to `false`.
     */
    private boolean decodeSlash = true;

    /**
     * If {@code true}, trailing slashes at the end of request urls will be removed.
     */
    private boolean removeTrailingSlash;

    public boolean isDefaultToProperties() {
        return defaultToProperties;
    }

    public void setDefaultToProperties(boolean defaultToProperties) {
        this.defaultToProperties = defaultToProperties;
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public Map<String, ApplicationClientConfiguration> getClients() {
        return clients;
    }

    public void setClients(Map<String, ApplicationClientConfiguration> clients) {
        this.clients = clients;
    }

    public Map<String, FeignClientConfiguration> getConfig() {
        return this.clients.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setConfig(Map<String, FeignClientConfiguration> config) {
    }

    public boolean isDecodeSlash() {
        return decodeSlash;
    }

    public void setDecodeSlash(boolean decodeSlash) {
        this.decodeSlash = decodeSlash;
    }

    public boolean isRemoveTrailingSlash() {
        return removeTrailingSlash;
    }

    public void setRemoveTrailingSlash(boolean removeTrailingSlash) {
        this.removeTrailingSlash = removeTrailingSlash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationClientProperties that = (ApplicationClientProperties) o;
        return defaultToProperties == that.defaultToProperties && Objects.equals(defaultConfig, that.defaultConfig)
                && Objects.equals(clients, that.clients) && Objects.equals(decodeSlash, that.decodeSlash)
                && Objects.equals(removeTrailingSlash, that.removeTrailingSlash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultToProperties, defaultConfig, clients, decodeSlash, removeTrailingSlash);
    }

    /**
     * Feign client configuration.
     */
    public static class ApplicationClientConfiguration extends FeignClientConfiguration {

        private Logger.Level loggerLevel;

        private Integer connectTimeout;

        private Integer readTimeout;

        private Class<Retryer> retryer;

        private Class<ErrorDecoder> errorDecoder;

        private List<Class<RequestInterceptor>> requestInterceptors;

        private Class<ResponseInterceptor> responseInterceptor;

        private Map<String, Collection<String>> defaultRequestHeaders = new HashMap<>();

        private Map<String, Collection<String>> defaultQueryParameters = new HashMap<>();

        private Boolean dismiss404;

        private Class<Decoder> decoder;

        private Class<Encoder> encoder;

        private Class<Contract> contract;

        private ExceptionPropagationPolicy exceptionPropagationPolicy;

        private List<Class<Capability>> capabilities;

        private Class<QueryMapEncoder> queryMapEncoder;

        private FeignClientProperties.MicrometerProperties micrometer;

        private Boolean followRedirects;

        private String apiToken;

        /**
         * Allows setting Feign client host URL. This value will only be taken into
         * account if the url is not set in the @FeignClient annotation.
         */
        private String url;

        public Logger.Level getLoggerLevel() {
            return loggerLevel;
        }

        public void setLoggerLevel(Logger.Level loggerLevel) {
            this.loggerLevel = loggerLevel;
        }

        public Integer getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Integer getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Class<Retryer> getRetryer() {
            return retryer;
        }

        public void setRetryer(Class<Retryer> retryer) {
            this.retryer = retryer;
        }

        public Class<ErrorDecoder> getErrorDecoder() {
            return errorDecoder;
        }

        public void setErrorDecoder(Class<ErrorDecoder> errorDecoder) {
            this.errorDecoder = errorDecoder;
        }

        public List<Class<RequestInterceptor>> getRequestInterceptors() {
            return requestInterceptors;
        }

        public void setRequestInterceptors(List<Class<RequestInterceptor>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
        }

        public Class<ResponseInterceptor> getResponseInterceptor() {
            return responseInterceptor;
        }

        public void setResponseInterceptor(Class<ResponseInterceptor> responseInterceptor) {
            this.responseInterceptor = responseInterceptor;
        }

        public Map<String, Collection<String>> getDefaultRequestHeaders() {
            return defaultRequestHeaders;
        }

        public void setDefaultRequestHeaders(Map<String, Collection<String>> defaultRequestHeaders) {
            this.defaultRequestHeaders = defaultRequestHeaders;
        }

        public Map<String, Collection<String>> getDefaultQueryParameters() {
            return defaultQueryParameters;
        }

        public void setDefaultQueryParameters(Map<String, Collection<String>> defaultQueryParameters) {
            this.defaultQueryParameters = defaultQueryParameters;
        }

        public Boolean getDismiss404() {
            return dismiss404;
        }

        public void setDismiss404(Boolean dismiss404) {
            this.dismiss404 = dismiss404;
        }

        public Class<Decoder> getDecoder() {
            return decoder;
        }

        public void setDecoder(Class<Decoder> decoder) {
            this.decoder = decoder;
        }

        public Class<Encoder> getEncoder() {
            return encoder;
        }

        public void setEncoder(Class<Encoder> encoder) {
            this.encoder = encoder;
        }

        public Class<Contract> getContract() {
            return contract;
        }

        public void setContract(Class<Contract> contract) {
            this.contract = contract;
        }

        public ExceptionPropagationPolicy getExceptionPropagationPolicy() {
            return exceptionPropagationPolicy;
        }

        public void setExceptionPropagationPolicy(ExceptionPropagationPolicy exceptionPropagationPolicy) {
            this.exceptionPropagationPolicy = exceptionPropagationPolicy;
        }

        public List<Class<Capability>> getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(List<Class<Capability>> capabilities) {
            this.capabilities = capabilities;
        }

        public Class<QueryMapEncoder> getQueryMapEncoder() {
            return queryMapEncoder;
        }

        public void setQueryMapEncoder(Class<QueryMapEncoder> queryMapEncoder) {
            this.queryMapEncoder = queryMapEncoder;
        }

        public FeignClientProperties.MicrometerProperties getMicrometer() {
            return micrometer;
        }

        public void setMicrometer(FeignClientProperties.MicrometerProperties micrometer) {
            this.micrometer = micrometer;
        }

        public Boolean isFollowRedirects() {
            return followRedirects;
        }

        public void setFollowRedirects(Boolean followRedirects) {
            this.followRedirects = followRedirects;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ApplicationClientConfiguration that = (ApplicationClientConfiguration) o;
            return loggerLevel == that.loggerLevel && Objects.equals(connectTimeout, that.connectTimeout)
                    && Objects.equals(readTimeout, that.readTimeout) && Objects.equals(retryer, that.retryer)
                    && Objects.equals(errorDecoder, that.errorDecoder)
                    && Objects.equals(requestInterceptors, that.requestInterceptors)
                    && Objects.equals(responseInterceptor, that.responseInterceptor)
                    && Objects.equals(dismiss404, that.dismiss404) && Objects.equals(encoder, that.encoder)
                    && Objects.equals(decoder, that.decoder) && Objects.equals(contract, that.contract)
                    && Objects.equals(exceptionPropagationPolicy, that.exceptionPropagationPolicy)
                    && Objects.equals(defaultRequestHeaders, that.defaultRequestHeaders)
                    && Objects.equals(defaultQueryParameters, that.defaultQueryParameters)
                    && Objects.equals(capabilities, that.capabilities)
                    && Objects.equals(queryMapEncoder, that.queryMapEncoder)
                    && Objects.equals(micrometer, that.micrometer)
                    && Objects.equals(followRedirects, that.followRedirects) && Objects.equals(url, that.url)
                    && Objects.equals(apiToken, that.apiToken);
        }

        @Override
        public int hashCode() {
            return Objects.hash(loggerLevel, connectTimeout, readTimeout, retryer, errorDecoder, requestInterceptors,
                    responseInterceptor, dismiss404, encoder, decoder, contract, exceptionPropagationPolicy,
                    defaultQueryParameters, defaultRequestHeaders, capabilities, queryMapEncoder, micrometer,
                    followRedirects, url, apiToken);
        }

    }

    /**
     * Micrometer configuration for Feign Client.
     */
    public static class MicrometerProperties extends FeignClientConfiguration {

        private Boolean enabled = true;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MicrometerProperties that = (MicrometerProperties) o;
            return Objects.equals(enabled, that.enabled);
        }

        @Override
        public int hashCode() {
            return Objects.hash(enabled);
        }

    }

}

