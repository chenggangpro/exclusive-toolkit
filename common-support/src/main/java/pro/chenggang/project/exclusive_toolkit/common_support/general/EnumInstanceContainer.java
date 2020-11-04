package pro.chenggang.project.exclusive_toolkit.common_support.general;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pro.chenggang.project.exclusive_toolkit.common_support.option.StringPool.AT;
import static pro.chenggang.project.exclusive_toolkit.common_support.option.StringPool.NULL;

/**
 * @author: chenggang
 * @date 2020-10-21.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class EnumInstanceContainer {

    static final String NULL_PLACEHOLDER = AT + NULL + AT;

    private final ConcurrentHashMap<Class<? extends Enum>, Map<String, Map<Object,List<Enum>>>> enumContainer = new ConcurrentHashMap<>();

    static EnumInstanceContainer getInstance(){
        return InstanceHolder.INSTANCE;
    }

    Map<String, Map<Object,List<Enum>>> getByEnumClass(Class<? extends Enum> enumClass){
        return enumContainer.get(enumClass);
    }

    void initEnumInstance(Class<? extends Enum> instanceClass){
        if(enumContainer.containsKey(instanceClass)){
            return;
        }
        synchronized (this){
            if(enumContainer.containsKey(instanceClass)){
                return;
            }
            Enum[] enumConstants = instanceClass.getEnumConstants();
            Map<String, Map<Object, List<Enum>>> collectResult = Stream.of(instanceClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(EnumSupport.class))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toMap(
                            Field::getName,
                            field -> Stream.of(enumConstants)
                                    .collect(Collectors.groupingBy(
                                            value -> {
                                                Object o = null;
                                                try {
                                                    o = field.get(value);
                                                } catch (IllegalAccessException e) {
                                                    //ignore
                                                }
                                                return o == null ? NULL_PLACEHOLDER : o;
                                            }
                                    )),
                            (o1, o2) -> o2,
                            HashMap::new
                    ));
            enumContainer.putIfAbsent(instanceClass,collectResult);
        }
    }

    private static class InstanceHolder{

        private static final EnumInstanceContainer INSTANCE;

        static {
            INSTANCE = new EnumInstanceContainer();
        }
    }
}
