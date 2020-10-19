package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.internal.DefaultShellCallback;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.GeneratorConfigurationFactory;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.PropertiesHolder;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.context.MyBatisSimpleContextGenerator;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.context.MybatisDynamicContextGenerator;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.properties.GeneratorExtensionProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: chenggang
 * @date 2020-01-21.
 */
public class MybatisGeneratorExtension {

    private final GeneratorConfigurationFactory configurationFactory;

    private MybatisGeneratorExtension(){
        this.configurationFactory = initConfigurationFactory();
    }

    private GeneratorConfigurationFactory initConfigurationFactory(){
        GeneratorConfigurationFactory configurationFactory = new GeneratorConfigurationFactory();
        configurationFactory.addContextGenerator(new MybatisDynamicContextGenerator());
        configurationFactory.addContextGenerator(new MyBatisSimpleContextGenerator());
        return configurationFactory;
    }

    public void generate(){
        initProperties(null,false);
        generateInternal();
    }

    public void generate(Class executeClass){
        if(Objects.isNull(executeClass)){
            throw new IllegalArgumentException("Execute Class Must Be Not Null");
        }
        initProperties(executeClass.getPackage().getName(),true);
        generateInternal();
    }

    public void generate(String basePackage){
        initProperties(basePackage,true);
        generateInternal();
    }

    private void initProperties(String basePackage,boolean forceBasePackage){
        GeneratorExtensionProperties properties = PropertiesHolder.getInstance().getProperties();
        if(forceBasePackage){
            properties.setBasePackage(basePackage);
        }
        properties.validateByDefault();
    }

    private void generateInternal(){
        Configuration configuration = this.configurationFactory.getConfiguration();
        GeneratorExtensionProperties properties = PropertiesHolder.getInstance().getProperties();
        DefaultShellCallback callback = new DefaultShellCallback(properties.isOverwrite());
        List<String> warnings = new ArrayList<>();
        try{
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
            myBatisGenerator.generate(new VerboseProgressCallback());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        warnings.forEach(System.out::println);
    }

    public static MybatisGeneratorExtension getInstance(){
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder{

        private static final MybatisGeneratorExtension INSTANCE;

        static {
            INSTANCE = new MybatisGeneratorExtension();
        }
    }

}
