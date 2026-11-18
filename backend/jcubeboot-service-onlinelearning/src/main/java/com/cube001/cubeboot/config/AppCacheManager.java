package com.cube001.cubeboot.config;


import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 10:26 2018/05/12
 * @Modified By:
 */
public class AppCacheManager implements CacheManager {

    private CacheManager redisCacheManager;
    private CacheManager ehCacheManager;
    private  String defaultCacheManager;
    private final static String redisPrefix = "redis";
    private final static String ehCachePrefix = "ehCache";

    public AppCacheManager(String defaultCacheManager)
    {
        this.defaultCacheManager=defaultCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        if (name.startsWith(redisPrefix))  {
            return redisCacheManager.getCache(name);
        } else if(name.startsWith(ehCachePrefix)) {
            return ehCacheManager.getCache(name);
        }
        else
        {
            if(defaultCacheManager.equals(redisPrefix))
            {
                return redisCacheManager.getCache(name);
            }
            else{
                return ehCacheManager.getCache(name);
            }
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> cacheNames = new ArrayList<String>();
        if (redisCacheManager != null) {
            cacheNames.addAll(redisCacheManager.getCacheNames());
        }
        if (ehCacheManager != null) {
            cacheNames.addAll(ehCacheManager.getCacheNames());
        }
        return cacheNames;
    }

    public CacheManager getRedisCacheManager() {
        return redisCacheManager;
    }

    public void setRedisCacheManager(CacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    public CacheManager getEhCacheCacheManager() {
        return ehCacheManager;
    }

    public void setEhCacheCacheManager(CacheManager ehcacheCacheManager) {
        this.ehCacheManager = ehcacheCacheManager;
    }
}

