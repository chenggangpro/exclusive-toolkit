package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.yaml.snakeyaml.Yaml;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.properties.GeneratorExtensionProperties;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author: chenggang
 * @date 2020-01-21.
 */
@Accessors()
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesHolder {

    private GeneratorExtensionProperties generatorExtensionProperties;
    private final Yaml yaml = new Yaml();

    public GeneratorExtensionProperties getProperties(boolean reload){
        if(reload){
            this.generatorExtensionProperties = readProperties();
        }
        return this.generatorExtensionProperties;
    }

    public GeneratorExtensionProperties getProperties(){
        if(Objects.isNull(this.generatorExtensionProperties)){
            return getProperties(true);
        }
        return getProperties(false);
    }

    private GeneratorExtensionProperties readProperties(){
        InputStream propertiesInputStream = this.getClass().getClassLoader().getResourceAsStream(GeneratorExtensionProperties.PROPERTIES_FILE_NAME_YML);
        if(Objects.isNull(propertiesInputStream)){
            propertiesInputStream = this.getClass().getClassLoader().getResourceAsStream(GeneratorExtensionProperties.PROPERTIES_FILE_NAME_YAML);
        }
        if(Objects.isNull(propertiesInputStream)){
            throw new IllegalArgumentException("mybatis-generator.yaml or mybatis-generator.yml should be classpath");
        }

        GeneratorExtensionProperties properties = yaml.loadAs(propertiesInputStream, GeneratorExtensionProperties.class);
        if(Objects.isNull(properties)){
            throw new IllegalArgumentException("Can Not Reader mybatis-generator.yaml or mybatis-generator.yml, GeneratorExtensionProperties Is Null ");
        }
        return properties;
    }

    public static PropertiesHolder getInstance(){
        return InstanceHolder.INSTANCE;
    }

    /**
     * instance holder
     */
    private static class InstanceHolder{

        private static final PropertiesHolder INSTANCE;

        static {
            INSTANCE = new PropertiesHolder();
        }
    }
}
