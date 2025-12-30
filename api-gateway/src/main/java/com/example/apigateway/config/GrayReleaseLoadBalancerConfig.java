package com.example.apigateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

public class GrayReleaseLoadBalancerConfig {

    public static class GrayReleaseLoadBalancer implements ReactorServiceInstanceLoadBalancer {

        private final ServiceInstanceListSupplier serviceInstanceListSupplier;

        public GrayReleaseLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier) {
            this.serviceInstanceListSupplier = serviceInstanceListSupplier;
        }

        @Override
        public Mono<Response<ServiceInstance>> choose(Request request) {
            return serviceInstanceListSupplier.get().next().map(instances -> getInstanceResponse(instances, request));
        }

        private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            return new Response<ServiceInstance>() {
                @Override
                public ServiceInstance getServer() {
                    return null;
                }
                
                @Override
                public boolean hasServer() {
                    return false;
                }
            };
        }

        // 从请求中获取版本信息
        String version = getVersionFromRequest(request);

        if (version != null && !version.isEmpty()) {
            // 过滤出匹配版本的服务实例
            List<ServiceInstance> filteredInstances = instances.stream()
                    .filter(instance -> version.equals(instance.getMetadata().get("version")))
                    .collect(Collectors.toList());

            if (!filteredInstances.isEmpty()) {
                // 简单轮询选择实例
                int index = Math.abs((int) System.currentTimeMillis() % filteredInstances.size());
                ServiceInstance instance = filteredInstances.get(index);
                return new Response<ServiceInstance>() {
                    @Override
                    public ServiceInstance getServer() {
                        return instance;
                    }
                    
                    @Override
                    public boolean hasServer() {
                        return true;
                    }
                };
            }
        }

        // 如果没有匹配版本或版本为空，返回默认实例
        int index = Math.abs((int) System.currentTimeMillis() % instances.size());
        ServiceInstance instance = instances.get(index);
        return new Response<ServiceInstance>() {
            @Override
            public ServiceInstance getServer() {
                return instance;
            }
            
            @Override
            public boolean hasServer() {
                return true;
            }
        };
    }

        private String getVersionFromRequest(Request request) {
            if (request.getContext() instanceof HttpHeaders headers) {
                return headers.getFirst("X-Service-Version");
            }
            return null;
        }
    }
}
