package pro.chenggang.project.exclusive_toolkit.common_support.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author: chenggang
 * @date 2020-10-06.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdkStreamSupport {

    /**
     * used in jdk8 stream function
     * with .filter() method  support distinctByKey operator
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
