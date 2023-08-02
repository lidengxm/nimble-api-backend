package com.lemng.nimbleapigateway;

import com.lmeng.nimbleclientsdk.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/1
 * @descriprion 网关全局过滤器
 */

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8103";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.打印请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识=" + request.getId());
        log.info("请求路径=" + request.getPath().value());
        log.info("请求参数=" + request.getQueryParams());
        log.info("请求方法=" + request.getMethod());
        log.info("请求头=" + request.getHeaders());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址=" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();

        //2.访问控制（白名单标识允许访问的IP）
        if(!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //3.用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //根据实际情况应该是去数据库中查询是否分配该用户 TODO
        if(!accessKey.equals(accessKey)) {
            return handleNoAuth(response);
        }
        //如果随机数超过10000报错
        if(Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        //时间戳和当前时间不能超过5分钟
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        final Long FIVE_MINIUTES = 60 * 5L;
        if(currentTimeMillis - Long.parseLong(timestamp) >= FIVE_MINIUTES) {
            return handleNoAuth(response);
        }
        //实际情况中是从数据库中查询secretKey
//        String serverSign = SignUtil.getSign(body, "0729");
//        if(!sign.equals(serverSign)) {
//            return handleNoAuth(response);
//        }

        //4.请求的模拟接口是否存在（从数据库中查询模拟接口是否存在以及请求方法是否匹配）

        //5.请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);

        //6.响应日志
        log.info("响应=" + (response.getStatusCode()));
        return handleResponse(exchange,chain);
        //7.调用接口成功，调用接口次数+1
//        if(response.getStatusCode() == HttpStatus.OK) {
//        } else {
//            //8.调用失败，返回一个规范的错误码
//            return handleInvokeError(response);
//        }
//        log.info("custom global filter");
//        return filter;
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    //, long interfaceInfoId, long userId
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的模拟接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //打印日志
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            //writeWith方法就是在 拼接字符串，将零散的内容拼接成完整的字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
//                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 执行调用接口成功 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 否则就降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}

