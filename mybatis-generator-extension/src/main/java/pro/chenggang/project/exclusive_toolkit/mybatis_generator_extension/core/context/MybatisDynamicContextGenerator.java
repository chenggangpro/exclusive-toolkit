package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.context;

import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.DomainObjectRenamingRule;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.option.GeneratorType;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.properties.GeneratorExtensionProperties;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.support.ExtensionCommentGenerator;

/**
 * @author: chenggang
 * @date 2019-10-22.
 */
public class MybatisDynamicContextGenerator implements ContextGenerator {

    @Override
    public GeneratorType targetGeneratorType() {
        return GeneratorType.DYNAMIC;
    }

    @Override
    public Context generateContext(GeneratorExtensionProperties extensionProperties) {
        String basePackage = extensionProperties.getBasePackage();
        Context dynamicContext = new Context(ModelType.FLAT);
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(extensionProperties.getConnectionUrl());
        jdbcConnectionConfiguration.setDriverClass(extensionProperties.getDriverClass());
        jdbcConnectionConfiguration.setUserId(extensionProperties.getUsername());
        jdbcConnectionConfiguration.setPassword(extensionProperties.getPassword());
        jdbcConnectionConfiguration.addProperty("nullCatalogMeansCurrent","true");
        if (extensionProperties.isGenerateComment()) {
            jdbcConnectionConfiguration.addProperty("remarksReporting", "true");
            jdbcConnectionConfiguration.addProperty("useInformationSchema", "true");
        }
        dynamicContext.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        dynamicContext.setTargetRuntime("MyBatis3DynamicSQL");
        dynamicContext.setId("MyBatis3Dynamic");
        dynamicContext.addProperty("javaFileEncoding","UTF-8");
        dynamicContext.addProperty("useMapperCommentGenerator","true");
        dynamicContext.addProperty("columnOverride","false");
        dynamicContext.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        if (extensionProperties.isGenerateComment()) {
            CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
            commentGeneratorConfiguration.setConfigurationType(ExtensionCommentGenerator.class.getCanonicalName());
            dynamicContext.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        }
        JavaClientGeneratorConfiguration dynamicJavaClientGenerator = new JavaClientGeneratorConfiguration();
        dynamicJavaClientGenerator.setTargetPackage(basePackage + "." + extensionProperties.getMapperInterfacePackage()+".dynamic");
        dynamicJavaClientGenerator.setTargetProject(extensionProperties.getParentLocation()+extensionProperties.getJavaLocation());
        dynamicJavaClientGenerator.setConfigurationType("XMLMAPPER");
        dynamicContext.setJavaClientGeneratorConfiguration(dynamicJavaClientGenerator);
        String trimPattern = extensionProperties.getTableTrimPattern();
        boolean shouldTrimTableName = StringUtility.stringHasValue(trimPattern);
        extensionProperties.getTableName().stream().map(item->{
            TableConfiguration tableConfiguration = new TableConfiguration(dynamicContext);
            tableConfiguration.setTableName(item);
            if(shouldTrimTableName){
                DomainObjectRenamingRule domainObjectRenamingRule = new DomainObjectRenamingRule();
                domainObjectRenamingRule.setSearchString(trimPattern);
                domainObjectRenamingRule.setReplaceString("");
                tableConfiguration.setDomainObjectRenamingRule(domainObjectRenamingRule);
            }
            return tableConfiguration;
        }).forEach(dynamicContext::addTableConfiguration);
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        javaTypeResolverConfiguration.setConfigurationType("pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.support.CustomJavaTypeResolver");
        javaTypeResolverConfiguration.addProperty("useJSR310Types","true");
        javaTypeResolverConfiguration.addProperty("forceBigDecimals","true");
        dynamicContext.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        JavaModelGeneratorConfiguration dynamicJavaModel = new JavaModelGeneratorConfiguration();
        dynamicJavaModel.setTargetPackage(basePackage+"."+ extensionProperties.getModelPackage());
        dynamicJavaModel.setTargetProject(extensionProperties.getParentLocation()+extensionProperties.getJavaLocation());
        dynamicContext.setJavaModelGeneratorConfiguration(dynamicJavaModel);
        PluginConfiguration dynamicPluginConfiguration = new PluginConfiguration();
        dynamicPluginConfiguration.setConfigurationType("pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.support.DynamicGenerateExtensionPlugin");
        dynamicContext.addPluginConfiguration(dynamicPluginConfiguration);
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin");
        dynamicContext.addPluginConfiguration(pluginConfiguration);
        PluginConfiguration renamePluginConfiguration = new PluginConfiguration();
        renamePluginConfiguration.setConfigurationType("pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.support.RenameJavaMapperPlugin");
        renamePluginConfiguration.addProperty("searchString","Mapper$");
        renamePluginConfiguration.addProperty("replaceString","DynamicMapper");
        dynamicContext.addPluginConfiguration(renamePluginConfiguration);
        return dynamicContext;
    }
}
