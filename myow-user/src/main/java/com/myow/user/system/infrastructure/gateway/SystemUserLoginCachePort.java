package com.myow.user.system.infrastructure.gateway;

import com.myow.common.port.UserLoginCachePort;
import com.myow.user.system.domain.consts.SystemCacheConst;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemUserLoginCachePort implements UserLoginCachePort {

    private final CacheManager cacheManager;

    @Override
    public void clearUserLoginCache(Long userId) {
        evict(SystemCacheConst.Login.USER_PERMISSION, userId);
        evict(SystemCacheConst.Login.LOGIN_USER, userId);
    }

    private void evict(String cacheName, Long userId) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(userId);
        }
    }
}
