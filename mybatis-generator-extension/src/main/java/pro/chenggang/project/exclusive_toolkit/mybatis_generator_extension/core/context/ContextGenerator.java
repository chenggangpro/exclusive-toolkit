package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.context;

import org.mybatis.generator.config.Context;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.option.GeneratorType;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.properties.GeneratorExtensionProperties;

/**
 * @author: chenggang
 * @date 2020-01-21.
 */
public interface ContextGenerator {

    /**
     * generator type
     * @return
     */
    GeneratorType targetGeneratorType();

    /**
     * generateContext
     * @param extensionProperties
     * @return
     */
    Context generateContext(GeneratorExtensionProperties extensionProperties);
}
