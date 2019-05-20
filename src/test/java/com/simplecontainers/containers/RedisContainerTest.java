package com.simplecontainers.containers;

import com.simplecontainers.rules.SimpleContainersSpinnerRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisContainerTest {

    private final static RedisContainer redisContainer = new RedisContainer();

    @ClassRule
    public final static SimpleContainersSpinnerRule rule = new SimpleContainersSpinnerRule(redisContainer);

    @Test
    public void shouldAbleToReturnPropertiesOfContainer() {
        Assert.assertNotNull(redisContainer.getExternalHost());
        Assert.assertNotNull(redisContainer.getExternalPort());
        Assert.assertNotNull(redisContainer.getInternalHost());
        Assert.assertNotNull(redisContainer.getInternalPort());
        Assert.assertNotNull(redisContainer.url());
    }

    @Test
    public void shouldAbleToSetAndGetValueFromRedis() {
        Jedis jedis = new Jedis(redisContainer.getExternalHost(), redisContainer.getExternalPort());
        jedis.set("Hello", "World");

        Assert.assertEquals("World", jedis.get("Hello"));
    }

}