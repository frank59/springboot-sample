package com.geewaza.code.study.camel.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc: mop
 * @author: lottery
 * @createTime: 2017年9月24日 下午12:47:32
 * @history:
 * @version: v1.0
 */
public class ReflectionUtils {
	/**
	 * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型 
	 * 如: public EmployeeDao extends BaseDao<Employee, String>
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}

		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型 如: public EmployeeDao extends
	 * BaseDao<Employee, String>
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperGenericType(Class<?> clazz) {
		return (Class<T>) getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Method getDeclaredMethod(Object object, String methodName,
			Class<?>[] parameterTypes) {

		try {
			for (Class<?> superClass = object.getClass(); superClass != Object.class;
					superClass = superClass.getSuperclass()) {
				// superClass.getMethod(methodName, parameterTypes);
				return superClass.getDeclaredMethod(methodName, parameterTypes);
				// Method 不在当前类定义, 继续向上转型
				// ..
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行方法<br />
	 * 直接调用对象方法, 而忽略修饰符(private, protected)
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object invokeMethod(Object object, String methodName,
									  Class<?>[] parameterTypes, Object[] parameters) {

		Method method = null;
		try {
			method = getDeclaredMethod(object, methodName, parameterTypes);

			if (method == null) {
				throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
			}

			method.setAccessible(true);

			return method.invoke(object, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<String> getDeclaredFieldNames(Class<?> clazz) {
		List<Field> fields = getDeclaredFields(clazz);
		List<String> filedNames = new LinkedList<String>();
		for (Field field : fields) {
			filedNames.add(field.getName());
		}
		return filedNames;
	}

	public static List<Field> getDeclaredFields(Class<?> clazz) {
		List<Field> fields = new LinkedList<Field>();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}

			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * @param instance
	 * @param fieldName
	 * @return
	 */
	public static Field getDeclaredField(Object instance, String fieldName) {
		List<Field> fields = getDeclaredFields(instance.getClass());
		for (Field field : fields) {
			if (fieldName.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 设置字段值<br />
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * @param instance
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void setFieldValue(Object instance, String fieldName, Object fieldValue) {
		Assert.notNull(instance, "实体类实例不可为null");
		Assert.notNull(fieldValue, "值不可为null");

		Field field = getDeclaredField(instance, fieldName);
		setFieldValue(instance, field, fieldValue);
	}

	public static void setFieldValue(Object instance, Field field, Object fieldValue, boolean find) {
		Assert.notNull(instance, "实体类实例不可为null");
		Assert.notNull(field, "实体类字段不可为null");
		Assert.notNull(fieldValue, "值不可为null");

		makeAccessible(field);

		Class<?> fieldType = field.getType();

		try {
			field.set(instance, fieldValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置字段值<br />
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * @param instance
	 * @param field
	 * @param fieldValue
	 */
	public static void setFieldValue(Object instance, Field field, Object fieldValue) {
		Assert.notNull(instance, "实体类实例不可为null");
		Assert.notNull(field, "实体类字段不可为null");
		Assert.notNull(fieldValue, "值不可为null");

		makeAccessible(field);

		Class<?> fieldType = field.getType();
		fieldValue = getFieldValue(fieldValue, fieldType);

		try {
			field.set(instance, fieldValue);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取字段值<br />
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter@param object
	 * @param instance
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object instance, String fieldName) {
		Assert.notNull(instance, "实体类实例不可为null.");
		Assert.hasLength(fieldName, "字段名不可为空.");

		Field field = getDeclaredField(instance, fieldName);
		return getFieldValue(instance, field);
	}

	/**
	 * 获取字段值<br />
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter@param object
	 * @param instance
	 * @param field
	 * @return
	 */
	public static Object getFieldValue(Object instance, Field field) {
		Assert.notNull(instance, "实体类实例不可为null.");
		Assert.notNull(field, "反射字段不可为null.");

		Object fieldValue = null;
		makeAccessible(field);
		try {
			fieldValue = field.get(instance);
			if (ObjectUtils.isEmpty(fieldValue)) {
				return fieldValue;
			}

			Class<?> fieldType = field.getType();
			fieldValue = getFieldValue(fieldValue, fieldType);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return fieldValue;
	}

	private static Object getFieldValue(Object fieldValue, Class<?> fieldType) {
		if (fieldType == String.class) {
			fieldValue = fieldValue.toString();
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			fieldValue = fieldType == byte.class ? Byte.parseByte(fieldValue.toString()) : Byte.valueOf(fieldValue.toString());
		} else if (fieldType == short.class || fieldType == Byte.class) {
			fieldValue = fieldType == byte.class ? Byte.parseByte(fieldValue.toString()) : Byte.valueOf(fieldValue.toString());
		} else if (fieldType == int.class || fieldType == Integer.class) {
			fieldValue = fieldType == int.class ? Integer.parseInt(fieldValue.toString()) : Integer.valueOf(fieldValue.toString());
		} else if(fieldType == long.class || fieldType == Long.class) {
			fieldValue = fieldType == long.class ? Long.parseLong(fieldValue.toString()) : Long.valueOf(fieldValue.toString());
		} else if (fieldType == float.class || fieldType == Float.class) {
			fieldValue = fieldType == float.class ? Float.parseFloat(fieldValue.toString()) : Float.valueOf(fieldValue.toString());
		} else if (fieldType == double.class || fieldType == Double.class) {
			fieldValue = fieldType == double.class ? Double.parseDouble(fieldValue.toString()) : Double.valueOf(fieldValue.toString());
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			fieldValue = fieldType == boolean.class ? Boolean.parseBoolean(fieldValue.toString()) : Boolean.valueOf(fieldValue.toString());
		} else if (fieldType == Date.class) {
			if (fieldValue.getClass() == Date.class) {
				return fieldValue;
			}
			if (fieldValue.toString().contains("T")) {
				fieldValue = DateUtils.parse(DateUtils.ES_YYYY_MMM_DD_HH_MM_SS, fieldValue.toString());
			} else {
				fieldValue = DateUtils.parse(DateUtils.YYYY_MMM_DD_HH_MM_SS, fieldValue.toString());
			}
		} else if (fieldType == List.class) {
            if (fieldValue.getClass() == String.class) {
                fieldValue = JSON.parseArray(fieldValue.toString());
            } else if (fieldValue.getClass() == LinkedList.class
                     || fieldValue.getClass() == ArrayList.class
                    || fieldValue.getClass() == List.class){
                fieldValue = JSON.parseArray(JSON.toJSONString(fieldValue));
            }
		}
		return fieldValue;
	}

	/**
	 * 使filed变为可访问
	 * @param field
	 */
	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}
}