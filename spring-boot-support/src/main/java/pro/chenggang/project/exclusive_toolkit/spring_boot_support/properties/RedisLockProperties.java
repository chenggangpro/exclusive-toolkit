package pro.chenggang.project.exclusive_toolkit.spring_boot_support.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * RedisLockProperties
 * @author chenggang
 * @date 2019/04/27
 */
@Getter
@Setter
@ToString
public class RedisLockProperties {

    public static final String REDIS_LOCK_PROPERTIES_PREFIX = "toolkit.spring.lock";
    /**
     * 注册统一Key
     */
    private String  registryKeys;
    /**
     * 最大过期时间，默认60s
     */
    private Long expireAfter = 60L;
    /**
     * 最大过期时间单位
     */
    private TimeUnit expireAfterTimeUnit = TimeUnit.SECONDS;

}
