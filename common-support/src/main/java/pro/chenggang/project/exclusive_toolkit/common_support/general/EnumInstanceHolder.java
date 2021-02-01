package pro.chenggang.project.exclusive_toolkit.common_support.general;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: chenggang
 * @date 2020-10-21.
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumInstanceHolder {

    /**
     * find specific type enum by fileName and value
     * @param enumType
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Enum> Optional<T> getEnum(Class<T> enumType,String fieldName,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumFieldNameContainerByClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldName, Collections.emptyMap());
        Object enumValue = Objects.isNull(value) ? EnumInstanceContainer.NULL_PLACEHOLDER : value;
        List<Enum> enumList = enumContainer.getOrDefault(enumValue, Collections.emptyList());
        return (Optional<T>) enumList.stream().findFirst();
    }

    /**
     * find all specific type enum by fileName and value
     * @param enumType
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Enum> List<T> getAllEnums(Class<T> enumType,String fieldName,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumFieldNameContainerByClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldName, Collections.emptyMap());
        Object enumValue = Objects.isNull(value) ? EnumInstanceContainer.NULL_PLACEHOLDER : value;
        List<Enum> enumList = enumContainer.getOrDefault(enumValue, Collections.emptyList());
        return (List<T>) enumList;
    }

    /**
     * find specific type enum by fileIndex and value
     * @param enumType
     * @param fieldIndex
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Enum> Optional<T> getEnum(Class<T> enumType,Integer fieldIndex,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<Integer, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumFieldOrderContainerByClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldIndex, Collections.emptyMap());
        Object enumValue = Objects.isNull(value) ? EnumInstanceContainer.NULL_PLACEHOLDER : value;
        List<Enum> enumList = enumContainer.getOrDefault(enumValue, Collections.emptyList());
        return (Optional<T>) enumList.stream().findFirst();
    }

    /**
     * find all specific type enum by fileIndex and value
     * @param enumType
     * @param fieldIndex
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Enum> List<T> getAllEnums(Class<T> enumType,Integer fieldIndex,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<Integer, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumFieldOrderContainerByClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldIndex, Collections.emptyMap());
        Object enumValue = Objects.isNull(value) ? EnumInstanceContainer.NULL_PLACEHOLDER : value;
        List<Enum> enumList = enumContainer.getOrDefault(enumValue, Collections.emptyList());
        return (List<T>) enumList;
    }

    /**
     * find first specific type enum by value
     * @param enumType
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Enum> Optional<T> getEnum(Class<T> enumType,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumFieldNameContainerByClass(enumType);
        Object enumValue = Objects.isNull(value) ? EnumInstanceContainer.NULL_PLACEHOLDER : value;
        return (Optional<T>) enumInstances.values()
                .stream()
                .filter(enumMap -> enumMap.containsKey(enumValue))
                .flatMap(enumMap -> enumMap.get(enumValue).stream())
                .findFirst();
    }

    /**
     * get enum by name
     * @param enumType
     * @param name
     * @param <T>
     * @return
     */
    public static <T extends Enum> Optional<T> getEnumByName(Class<T> enumType,String name){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Enum> enumInstances = EnumInstanceContainer.getInstance().getByEnumNameContainerByClass(enumType);
        return (Optional<T>) Optional.ofNullable(enumInstances.get(name));
    }

}
