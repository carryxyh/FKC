import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
public @interface Validator {

    public Check[] value() default {};
}

import com.dfire.validator.annotation.Check;
import com.dfire.validator.annotation.Validator;
import com.dfire.validator.exception.ValidatorException;
import com.dfire.validator.validator.abstracts.ValidatorAdapter;
import com.twodfire.share.result.Result;
import com.twodfire.share.result.ResultSupport;
import net.sf.oval.ConstraintViolation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Aspect
public class ValidatorAspect {

    /**
     * 拦截所有使用Validator注解的接口，并进行参数合法有效性验证
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.dfire.validator.annotation.Validator)")
    public Object validator(ProceedingJoinPoint joinPoint) throws Throwable {


        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Object[] objects = joinPoint.getArgs();


        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        //获得接口实现类的所有方法
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        Map<String,Integer> paramMap = new HashMap<>();
        for(Method method1:methods) {
            //判断接口名与参数是否一致
            if(StringUtils.equals(method.getName(),method1.getName())
                    && method.getParameterTypes().length == method1.getParameterTypes().length
                    && checkMethod(method,method1)){
                String[] params = u.getParameterNames(method1);
                for (int i = 0; i < params.length; i++) {
                    paramMap.put(params[i],i);
                }
                break;
            }
        }
        //取得需要验证的注解对象
        Validator interfaceValidator = method.getAnnotation(Validator.class);
        //取得明细验证规则
        Check[] params = interfaceValidator.value();
        int index = 0;
        for(int i=0; i<params.length;i++){
            Check p = params[i];
            Integer idx = paramMap.get(p.name());
            index = idx != null ? idx : i ;
            //如未指定验证参数名字，且当前规则所在索引大于参数个数时，报出异常
            if(index >= objects.length){
                throw new Exception("未指定name或name错误 的注解索引已 大于 参数个数，请检查！注：name需与实现参数名一致！");
            }
            Object o = objects[index];//验证参数对象

            Class<? extends ValidatorAdapter> vda = p.adapter();
            //如未指定适配器，则默认使用oval验证对象
            if (vda.getName().equals(ValidatorAdapter.class.getName())) {
                if(o != null) { //当验证对象不为null，使用oval验证框架验证
                    net.sf.oval.Validator validator = new net.sf.oval.Validator();
                    List<ConstraintViolation> ret = validator.validate(o);
                    if (ret != null && ret.size() > 0) {
                        return processResult(p.name(),ret.get(0).getErrorCode(), ret.get(0).getMessage(), method.getReturnType());
                    }
                }
            } else {
                ValidatorAdapter adapter = (ValidatorAdapter) Class.forName(vda.getName()).newInstance();
                //根据注解相应设置的条件调用相应的验证方法
                if ((p.v().length == 0 && !adapter.validate(o))
                        || (p.v().length == 1 && !adapter.validate(o, p.v()[0]))
                        || (p.v().length == 2 && !adapter.validate(o, p.v()[0], p.v()[1]))) {
                    String message = StringUtils.isBlank(p.message()) ? adapter.getMessage() : p.message();
                    return processResult(p.name(),p.errorCode(), message, method.getReturnType());
                }
            }
        }
        //调用目标方法
        return joinPoint.proceed();

    }

    /**
     * 判断两个方法的所有相应索引参数类型是否一致
     * @param method1
     * @param method2
     * @return
     */
    private boolean checkMethod(Method method1,Method method2){
        Class<?>[] types1 = method1.getParameterTypes();
        Class<?>[] types2 = method2.getParameterTypes();
        for(int i=0;i<types1.length;i++){
            Class type1 = types1[i];
            Class type2 = types2[i];
            if(!StringUtils.equals(type1.getName(),type2.getName())){
                return false;
            }
        }
        return true;
    }

    /**
     * 根据不同的返回类型，做相应的处理
     * @param fieldName
     * @param errorCode
     * @param message
     * @param classType
     * @return
     */
    private Result processResult(String fieldName,String errorCode,String message,Class classType){
        if(Result.class.equals(classType)){
            return new ResultSupport(errorCode,fieldName+message);
        }
        throw new ValidatorException(errorCode,fieldName+message);
    }


}


import com.dfire.validator.validator.abstracts.ValidatorAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {

    /**
     * 规则适配器，使用者可以自己写验证规则，继承 Validator并实现相应接口即可
     * @return
     */
    public Class<? extends ValidatorAdapter> adapter() default ValidatorAdapter.class;

    /**
     * 比较参数，目前只能设置double类型参数，用于验证数据大小或字符串长度，验证长度时，会把比较double数据转为int类型
     * @return
     */
    public double[] v() default {};

    /**
     * 需要验证的参数名字，不设置按check所在的索引匹配参数
     * @return
     */
    public String name() default "";

    /**
     * 错误编码
     * @return
     */
    public String errorCode() default "";

    /**
     * 错误内容
     * @return
     */
    public String message() default "";


}
