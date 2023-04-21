package com.dongruan.graduation.networkdiskssoserver.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: duyubo
 * @date: 2020年11月19日, 0019 11:25
 * @description:
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PhoneValidator.class})
public @interface Phone {

    String regexp() default "^1[345678]\\d{9}$";

    String message() default "手机号格式错误！";

    /**
     * groups 为分组验证，不配置为Default组，
     * 这样可以根据不同的应用场景来定义一些group
     * 比如在执行save与update操作时，可能很多参数在update时就不是必须的了，
     * 这样就可以定义两个接口，分别表示Save.class组与Update.class组，
     * 在save操作时，使用@valid(group={Save.class})，这样就可以只对在Save.class标记的注解中进行约束验证
     * @return
     */
    Class<?>[] groups() default { };

    /**
     * payload这个参数
     * 应用并不多，可以通过它 来携带给验证器一些元数据信息
     * 比如自定义验证器时，验证对象可以是String、也可以是Optional<String>
     * 这时仅仅只用@NotNull 就无法正确验证了，这时候可以通过payload来标记一些需要特殊处理的操作
     * @return
     */
    Class<? extends Payload>[] payload() default { };

}
