package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.support;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.core.PropertiesHolder;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.option.LombokConfig;
import pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.properties.GeneratorExtensionProperties;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class GenerateExtensionPlugin extends PluginAdapter {

    private boolean extendDynamicMapper;

    @Override
    public boolean validate(List<String> warnings) {
        this.extendDynamicMapper = Boolean.parseBoolean(this.properties.getProperty("extendDynamicMapper"));
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        if((introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3)){
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
            interfaze.addAnnotation("@Mapper");
            if(extendDynamicMapper){
                String fullyQualifiedName = interfaze.getType().getFullyQualifiedName();
                String beforeLast = StringUtils.substringBeforeLast(fullyQualifiedName, "Mapper");
                String dynamicMapperName = beforeLast+"DynamicMapper";
                String importDynamicMapperName = interfaze.getType().getPackageName() + ".dynamic." + StringUtils.substringAfterLast(dynamicMapperName, ".");
                interfaze.addImportedType(new FullyQualifiedJavaType(importDynamicMapperName));
                interfaze.addSuperInterface(new FullyQualifiedJavaType(dynamicMapperName));
            }
        }
        return true;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }
    
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }
    
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processExtension(topLevelClass, introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processExtension(topLevelClass, introspectedTable);
        return true;
    }
    
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processExtension(topLevelClass, introspectedTable);
        return false;
    }

    private void processExtension(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        GeneratorExtensionProperties extensionProperties = PropertiesHolder.getInstance().getProperties();
        LinkedHashSet<LombokConfig> lombokConfigs = extensionProperties.getLombok();
        if(Objects.isNull(lombokConfigs) || lombokConfigs.isEmpty()){
            return;
        }
        lombokConfigs.forEach(lombok->{
            switch (lombok){
                case Getter:
                    topLevelClass.addImportedType("lombok.Getter");
                    topLevelClass.addAnnotation("@Getter");
                    break;
                case Setter:
                    topLevelClass.addImportedType("lombok.Setter");
                    topLevelClass.addAnnotation("@Setter");
                    break;
                case ToString:
                    topLevelClass.addImportedType("lombok.ToString");
                    topLevelClass.addAnnotation("@ToString");
                    break;
                case Data:
                    topLevelClass.addImportedType("lombok.Data");
                    topLevelClass.addAnnotation("@Data");
                    break;
                case Builder:
                    topLevelClass.addImportedType("lombok.Builder");
                    topLevelClass.addAnnotation("@Builder");
                    break;
                case NoArgsConstructor:
                    topLevelClass.addImportedType("lombok.NoArgsConstructor");
                    topLevelClass.addAnnotation("@NoArgsConstructor");
                    break;
                case AllArgsConstructor:
                    topLevelClass.addImportedType("lombok.AllArgsConstructor");
                    topLevelClass.addAnnotation("@AllArgsConstructor");
                    break;
                case EqualsAndHashCode:
                    topLevelClass.addImportedType("lombok.EqualsAndHashCode");
                    topLevelClass.addAnnotation("@EqualsAndHashCode");
                    break;
                case AccessorsChain:
                    topLevelClass.addImportedType("lombok.experimental.Accessors");
                    topLevelClass.addAnnotation("@Accessors(chain = true)");
                    break;
                case AccessorsFluent:
                    topLevelClass.addImportedType("lombok.experimental.Accessors");
                    topLevelClass.addAnnotation("@Accessors(fluent = true)");
                    break;
                default:
            }
        });
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            Field field = new Field(introspectedColumn.getActualColumnName().toUpperCase(),new FullyQualifiedJavaType(String.class.getName()));
            field.setVisibility(JavaVisibility.PUBLIC);
            field.setStatic(true);
            field.setFinal(true);
            field.setInitializationString("\"" + introspectedColumn.getJavaProperty() + "\"");
            context.getCommentGenerator().addClassComment(topLevelClass, introspectedTable);
            topLevelClass.addField(field);
            Field columnField = new Field("DB_" + introspectedColumn.getActualColumnName().toUpperCase(),new FullyQualifiedJavaType(String.class.getName()));
            columnField.setVisibility(JavaVisibility.PUBLIC);
            columnField.setStatic(true);
            columnField.setFinal(true);
            columnField.setInitializationString("\"" + introspectedColumn.getActualColumnName() + "\"");
            topLevelClass.addField(columnField);
        }
    }

}
