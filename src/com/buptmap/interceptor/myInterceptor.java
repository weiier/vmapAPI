//package com.buptmap.interceptor;
//
//import org.aspectj.lang.ProceedingJoinPoint;  
//import org.aspectj.lang.annotation.After;  
//import org.aspectj.lang.annotation.AfterReturning;  
//import org.aspectj.lang.annotation.AfterThrowing;  
//import org.aspectj.lang.annotation.Around;  
//import org.aspectj.lang.annotation.Aspect;  
//import org.aspectj.lang.annotation.Before;  
//import org.aspectj.lang.annotation.Pointcut;  
//  
////@Aspect用来标示一个类为切面  
//@Aspect  
//public class MyInterceptor {  
//    // @Pointcut用来设置一个切入点。aop.annotation.service..包（子包）内的所有类，所有方法，任何参数  
//    @Pointcut("execution(* aop.annotation.service.imple..*.*(..))")  
//    private void anyMethod() {  
//  
//    }  
//  
//    // 使用@Before(切入点)用来表示目标方法执行前执行的操作（前置通知）  
//    // @Before("anyMethod()")  
//    @Before("anyMethod() && args(nameArg)")  
//    // 使用这个方法可以获取参数。即：在原来的切入点条件上加了另一个条件即：拦截方法的参数有一个并且是String类型  
//    public void doBefore(String nameArg) {  
//        System.out.println("前置通知...拦截方法执行参数：" + nameArg);  
//    }  
//  
//    // 使用@AfterReturning(切入点)用来表示目标方法执行完执行的操作（后置通知）  
//    // @AfterReturning("anyMethod()")  
//    @AfterReturning(pointcut = "anyMethod()", returning = "returnArg")  
//    // 使用这个方法可以获取返回结果。即：在原来的切入点条件上加了另一个条件即：拦截方法的返回值类型是String类型  
//    public void doAfterReturning(String returnArg) {  
//        System.out.println("后置通知...拦截方法返回结查：" + returnArg);  
//    }  
//  
//    // 使用@After(切入点)用来表示目标方法执行无论是否出现异常都执行的操作（最终通知）  
//    @After("anyMethod()")  
//    public void doFinally() {  
//        System.out.println("最终通知...");  
//    }  
//  
//    // 使用@AfterThrowing(切入点)用来表示目标方法执行出现异常时执行的操作（例外通知）  
//    // @AfterThrowing("anyMethod()")  
//    @AfterThrowing(pointcut = "anyMethod()", throwing = "ex")  
//    public void doException(Exception ex) {  
//        System.out.println("例外通知...取获异常信息：" + ex);  
//    }  
//  
//    // 使用@Around(切入点)用来表示整个通知（环绕通知：该方法必须接受一个org.aspectj.lang.ProceedingJoinPoint类型的参数）  
//    @Around("anyMethod()")  
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
//        System.out.println("环绕通知之前...");  
//        Object result = pjp.proceed();  
//        System.out.println("环绕通知之后...");  
//        return result;  
//    }  
//  
//}
