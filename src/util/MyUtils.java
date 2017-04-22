package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rll on 2017/4/22.
 */
public class MyUtils {
    /**
     * 检查字符串对象是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (null == input || "".equals(input))
            return true;
        return false;
    }

    /**
     * 增加天数
     *
     * @param date 时间基数
     * @param days 增加天数
     * @param type 0 天数
     * @return
     */
    public static Date addDays(Date date, int days, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (type == 0) {
            calendar.set(Calendar.DAY_OF_MONTH, days);
        }
        return calendar.getTime();
    }

    /**
     * 通过另一个类对当前类赋值
     *
     * @param className 实体类对象名
     * @param a         最终需要更新的对象
     * @param b         接收到的对象
     */
    public static boolean editSetAttribute(String className, Object a, Object b) {
        try {
            Class<?> clazz = Class.forName(className);
            setSelfAttribute(clazz,a,b);
        } catch (Exception e) {
            System.out.println("找不到此类");
            return false;
        }
        return true;
    }

    /**
     * 通过另一个类对当前类赋值(包括父类)
     *
     * @param className 实体类对象名
     * @param a         最终需要更新的对象
     * @param b         接收到的对象
     */
    public static boolean editSetAttributeWithFather(String className, Object a, Object b) {
        try {
            Class<?> clazz = Class.forName(className);
            do{
                setSelfAttribute(clazz,a,b);
                clazz = clazz.getSuperclass();
            }while (null != clazz.getSuperclass());
        } catch (Exception e) {
            System.out.println("找不到此类");
            return false;
        }
        return true;
    }

    /**
     * @param clazz 需要进行赋值的a的类
     * @param a     最终需要更新的对象
     * @param b     接收到的对象
     */
    private static void setSelfAttribute(Class<?> clazz, Object a, Object b) {
        // 取得本类的全部属性
        Field[] field = clazz.getDeclaredFields();
        for (Field tmp_f : field) { //有额外类型可添加
            try {
                //属性类型
                String typeName = tmp_f.getType().toString();
//                System.out.println(typeName);
                //组装大写属性名
                String attributeName = tmp_f.getName().substring(0, 1).toUpperCase() + tmp_f.getName().substring(1);
//                System.out.println(attributeName);
                Method getMethod = b.getClass().getMethod("get" + attributeName);
                //针对不同类型 进行set赋值
                if (typeName.lastIndexOf("String") != -1 && null != getMethod.invoke(b)) {
                    Method setMethod = clazz.getMethod("set" + attributeName, String.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("int") != -1) {
                    Method setMethod = clazz.getMethod("set" + attributeName, int.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("long") != -1 && 0 != (long) getMethod.invoke(b)) {
                    Method setMethod = clazz.getMethod("set" + attributeName, long.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("float") != -1 && 0 != (float) getMethod.invoke(b)) {
                    Method setMethod = clazz.getMethod("set" + attributeName, float.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("Date") != -1 && null != (Date) getMethod.invoke(b)) {
                    Method setMethod = clazz.getMethod("set" + attributeName, Date.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("double") != -1 && 0 != (double) getMethod.invoke(b)) {
                    Method setMethod = clazz.getMethod("set" + attributeName, double.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("boolean") != -1) {
                    Method setMethod = clazz.getMethod("set" + attributeName, boolean.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
                if (typeName.lastIndexOf("char") != -1) {
                    Method setMethod = clazz.getMethod("set" + attributeName, char.class);
                    setMethod.invoke(a, getMethod.invoke(b));
                    continue;
                }
            } catch (Exception e) {
//                e.printStackTrace();
                continue;
            }
        }
    }
}
