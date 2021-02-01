package pro.chenggang.project.exclusive_toolkit.common_support.general;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    private final ConcurrentHashMap<Class<? extends Enum>, Map<String, Map<Object,List<Enum>>>> enumFieldNameContainer = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Enum>, Map<Integer, Map<Object,List<Enum>>>> enumFieldOrderContainer = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends Enum>, Map<String,Enum>> enumNameContainer = new ConcurrentHashMap<>();

    static EnumInstanceContainer getInstance(){
        return InstanceHolder.INSTANCE;
    }

    Map<String, Map<Object,List<Enum>>> getByEnumFieldNameContainerByClass(Class<? extends Enum> enumClass){
        return enumFieldNameContainer.get(enumClass);
    }

    Map<Integer, Map<Object,List<Enum>>> getByEnumFieldOrderContainerByClass(Class<? extends Enum> enumClass){
        return enumFieldOrderContainer.get(enumClass);
    }

    Map<String,Enum> getByEnumNameContainerByClass(Class<? extends Enum> enumClass){
        return enumNameContainer.get(enumClass);
    }

    void initEnumInstance(Class<? extends Enum> instanceClass){
        if(enumFieldNameContainer.containsKey(instanceClass)){
            return;
        }
        synchronized (this){
            if(enumFieldNameContainer.containsKey(instanceClass)){
                return;
            }
            Enum[] enumConstants = instanceClass.getEnumConstants();
            Field[] declaredFields = instanceClass.getDeclaredFields();
            List<Field> fieldList = Stream.of(declaredFields)
                    .filter(field -> field.isAnnotationPresent(EnumField.class))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            Map<String, Map<Object, List<Enum>>> nameResult = fieldList
                    .stream()
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
            enumFieldNameContainer.putIfAbsent(instanceClass,nameResult);
            List<Field> noStaticFieldList = Stream.of(declaredFields).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
            Map<Field,Integer> indexFieldMap = IntStream.range(0, noStaticFieldList.size())
                    .boxed()
                    .collect(Collectors.toMap(
                            noStaticFieldList::get,
                            index -> index
                    ));
            HashMap<Integer, Map<Object, List<Enum>>> ordinalResult = fieldList.stream()
                    .collect(Collectors.toMap(
                            indexFieldMap::get,
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
            enumFieldOrderContainer.putIfAbsent(instanceClass,ordinalResult);
            Map<String, Enum> enumNameMap = Stream.of(enumConstants).collect(Collectors.toMap(Enum::name, enumConstant -> enumConstant));
            enumNameContainer.putIfAbsent(instanceClass,enumNameMap);
        }
    }

    private static class InstanceHolder{

        private static final EnumInstanceContainer INSTANCE;

        static {
            INSTANCE = new EnumInstanceContainer();
        }
    }
}
