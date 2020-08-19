package cn.guet.gdmbs.log;

import cn.guet.gdmbs.entity.SysLog;
import cn.guet.gdmbs.mapper.SysLogMapper;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Pointcut("@annotation(cn.guet.gdmbs.log.Log)")
    public void pointcut() { }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point, time);
        return result;
    }


    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLog.setOperation(logAnnotation.value());
            sysLog.setUsername(logAnnotation.name());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className+ "."+ methodName+  "()");
        // 请求的方法参数值

        // 获取request
        sysLog.setIp("127.0.0.1");
        // 模拟一个用户名
        sysLog.setUsername("mrbird");
        sysLog.setLogTime(new Date().toString());
        // 保存系统日志
        System.out.println(JSON.toJSON(sysLog));
        sysLogMapper.saveSysLog(sysLog);
    }
}
