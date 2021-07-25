package com.imorning.whiteboard.utils;

import java.lang.reflect.Method;

/**
 * @author iMorning
 * @version 2021/07/23
 */

public class BeanUtil {
    /**
     *
     */

    public static void CopyBeanToBean(Object obj1, Object obj2) throws Exception {

        Method[] method1 = obj1.getClass().getMethods();

        Method[] method2 = obj2.getClass().getMethods();

        String methodName1;

        String methodFix1;

        String methodName2;

        String methodFix2;

        for (Method method : method1) {

            methodName1 = method.getName();

            methodFix1 = methodName1.substring(3, methodName1.length());

            if (methodName1.startsWith("get")) {

                for (Method value : method2) {

                    methodName2 = value.getName();

                    methodFix2 = methodName2.substring(3, methodName2.length());

                    if (methodName2.startsWith("set")) {

                        if (methodFix2.equals(methodFix1)) {

                            Object[] objs1 = new Object[0];

                            Object[] objs2 = new Object[1];

                            objs2[0] = method.invoke(obj1, objs1);//激活obj1的相应的get的方法，objs1数组存放调用该方法的参数,此例中没有参数，该数组的长度为0

                            value.invoke(obj2, objs2);//激活obj2的相应的set的方法，objs2数组存放调用该方法的参数

                        }

                    }

                }

            }

        }

    }

}
