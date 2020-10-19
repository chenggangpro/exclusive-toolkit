package pro.chenggang.project.exclusive_toolkit.mybatis_generator_extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @classDesc:
 * @author: chenggang
 * @createTime: 2019-02-25
 * @version: v1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Required {

}
