package com.lemng.apigateway.filter;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.lemng.apigateway.exception.BusinessException;
import com.lmeng.apicommon.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @learner lmeng
 * @date 2023/9/17
 * @des
 */
@Slf4j
@Component
public class LoginGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private RateLimiter rateLimiter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //尝试获取令牌，如果获取令牌成功，说明请求未超过限定的速率，允许请求继续执行。
        if(!rateLimiter.tryAcquire()) {
            log.info("一瞬间请求太多了，请稍等一下...");
            //如果获取令牌失败，说明请求速率超过了限制
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }

        return chain.filter(exchange);
    }

    /**
     * 指定了该全局过滤器的执行顺序，负数值表示在其他过滤器之前执行，确保限流是在其他操作之前进行的
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
