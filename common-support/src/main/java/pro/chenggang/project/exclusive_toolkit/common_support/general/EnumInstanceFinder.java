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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumInstanceFinder {

    @SuppressWarnings("unchecked")
    public static <T extends Enum> Optional<T> getEnum(Class<T> enumType,String fieldName,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldName, Collections.emptyMap());
        if(Objects.isNull(value)){
            value = EnumInstanceContainer.NULL_PLACEHOLDER;
        }
        List<Enum> enumList = enumContainer.getOrDefault(value, Collections.emptyList());
        return (Optional<T>) enumList.stream().findFirst();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum> List<T> getAllEnums(Class<T> enumType,String fieldName,Object value){
        EnumInstanceContainer.getInstance().initEnumInstance(enumType);
        Map<String, Map<Object, List<Enum>>> enumInstances = EnumInstanceContainer.getInstance().getByEnumClass(enumType);
        Map<Object, List<Enum>> enumContainer = enumInstances.getOrDefault(fieldName, Collections.emptyMap());
        if(Objects.isNull(value)){
            value = EnumInstanceContainer.NULL_PLACEHOLDER;
        }
        List<Enum> enumList = enumContainer.getOrDefault(value, Collections.emptyList());
        return (List<T>) enumList;
    }


}
