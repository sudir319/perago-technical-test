package com.perago.techtest;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

public class DiffEngineImpl implements DiffEngine {

	public <T extends Serializable> T apply(T original, Diff<?> diff) throws DiffException {

		T t = null;
		try {
			if (diff == null) {
				throw new DiffException("Diff cannot be null");
			}
			
			if (original == null)
			{
				if (diff.getHolder() != null)
				{
					original = (T) diff.getHolder();
				}
			}
			
			if(original == null) {
				return original;
			}

			t = (T) BeanUtils.cloneBean(original);
			
			if(diff.getChangeLogs().get(0).getStatus() == Status.Delete)
			{
				return null;
			}
			
			applyDifferencesOnObject(diff, t, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	public <T extends Serializable> Diff<T> calculate(T original, T modified) throws DiffException {

		if (original != null && modified != null) {
			if (!original.getClass().equals(modified.getClass())) {
				throw new DiffException(
						"Objects are of different types. original object is of type : " + original.getClass().getName()
								+ ", modified object is of type : " + original.getClass().getName());
			}
		}
		Diff<T> diff = new Diff<T>();
		T t = null;
		try {
			if (original != null) {
				t = (T) BeanUtils.cloneBean(original);
			}
			diff.setHolder(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			findDifferences(diff, original, modified, 1, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return diff;
	}

	private <T extends Serializable> void findDifferences(final Diff<T> diff, final T original, final T modified,
			int depth, boolean ignoreUpdate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Field[] children;
		
		if (original != null) {
			children = original.getClass().getDeclaredFields();
		} else {
			children = modified.getClass().getDeclaredFields();
		}
		
		if (original == null) {
			diff.addLog(new Diff.ChangeLog(Status.Create, modified.getClass().getName(), null, depth, true));
			depth++;
			for (Field eachChild : children) {
				if (Modifier.isStatic(eachChild.getModifiers())) {
					continue;
				}
				if (eachChild.getType().equals(modified.getClass())) {
					diff.addLog(new Diff.ChangeLog(Status.Create, eachChild.getName(), eachChild.getType().getSimpleName(),
							depth, false));
				} else {
					eachChild.setAccessible(true);
					Object modifiedValue = eachChild.get(modified);
					diff.addLog(new Diff.ChangeLog(Status.Create, eachChild.getName(), eachChild.getType().getSimpleName(),
							null, modifiedValue, depth, false));
				}
				if (eachChild.getType().equals(modified.getClass())) {
					eachChild.setAccessible(true);
					T value = (T) eachChild.get(modified);
					if (value != null) {
						findDifferences(diff, null, value, depth + 1, false);
					}
				}
			}

		} else if (modified == null) {
			diff.addLog(new Diff.ChangeLog(Status.Delete, original.getClass().getName(), null, depth, true));
		} else {
			if(!ignoreUpdate)
			{
				diff.addLog(new Diff.ChangeLog(Status.Update, modified.getClass().getName(), null, depth, true));
				depth++;
			}
			for (Field child : children) {
				if (Modifier.isStatic(child.getModifiers())) {
					continue;
				}
				boolean isCollection = false;
				// collections are proving somewhat difficult probably due to
				// referencing(pointers)
				if (original != null || modified != null) {
					child.setAccessible(true);
					Object instance = (original != null) ? child.get(original) : child.get(modified);
					isCollection = instance instanceof Collection;
				}
				
				if ((getPropertyValue(original, child.getName()) == null)
						&& getPropertyValue(modified, child.getName()) == null) {
					continue;
				}
				//Friend
				if (child.getType().equals(original.getClass())) {
					diff.addLog(new Diff.ChangeLog(Status.Update, child.getName(), child.getType().getSimpleName(),
							depth, false));
					child.setAccessible(true);

					T fieldInOriginal = (T) child.get(original);
					T fieldInModified = (T) child.get(modified);

					if (fieldInModified == null && fieldInOriginal != null) {
						diff.addLog(new Diff.ChangeLog(Status.Delete, child.getName(), child.getType().getSimpleName(),
								depth + 1, false));
						continue;
					}
					findDifferences(diff, fieldInOriginal, fieldInModified, depth + 1, true);
					continue;
				}
				if (getPropertyValue(original, child.getName()) == null 
						&& getPropertyValue(modified, child.getName()) != null) {
					child.setAccessible(true);
					Object modifiedInstance = child.get(modified);
					diff.addLog(new Diff.ChangeLog(Status.Update, child.getName(), child.getType().getSimpleName(),
							null, modifiedInstance, depth, false));
				}
				if (getPropertyValue(original, child.getName()) != null
						&& getPropertyValue(modified, child.getName()) != null) {

					if (!isCollection && (getPropertyValue(original, child.getName())
							.equals(getPropertyValue(modified, child.getName())))) {
						continue;
					} else {
						child.setAccessible(true);
						Object modifiedValue = child.get(modified);
						Object originalValue = child.get(original);
						
						if (isCollection) {
							if (CollectionUtils.isEqualCollection((Collection) originalValue,
									(Collection) modifiedValue)) {
								continue;
							}
						}
						
						if(!isStandardDataType(child.getType()))
						{
							diff.addLog(new Diff.ChangeLog(Status.Update, child.getName(), child.getType().getSimpleName(),
									depth, false));
							
							T fieldInOriginal = (T) child.get(original);
							T fieldInModified = (T) child.get(modified);
							
							if (fieldInModified == null && fieldInOriginal != null) {
								diff.addLog(new Diff.ChangeLog(Status.Delete, child.getName(), child.getType().getSimpleName(),
										depth, false));
								continue;
							}
							
							findDifferences(diff, fieldInOriginal, fieldInModified, depth + 1, true);
						}
						else
						{
							diff.addLog(new Diff.ChangeLog(Status.Update, child.getName(), child.getType().getSimpleName(),
									originalValue, modifiedValue, depth, false));
						}
					}
				}
				if (getPropertyValue(modified, child.getName()) == null
						&& getPropertyValue(original, child.getName()) != null) {
					diff.addLog(new Diff.ChangeLog(Status.Delete, child.getName(), child.getType().getTypeName(), depth,
							false));
				}

			}

		}
	}
	
	private boolean isStandardDataType(Class<?> type) {
		return type.getName().startsWith("java.lang") || type.getName().startsWith("java.util") || type.getName().startsWith("java.sql");
	}

	private Object getPropertyValue(Object object, String property)
	{
		Object returnObject = null;
		try {
			returnObject = BeanUtils.getProperty(object, property);
		} catch (Exception e) {
		}
		
		return returnObject;
	}

	private static boolean setField(Object targetObject, String fieldName, Object fieldValue) {
		Field field;
		try {
			field = targetObject.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			field = null;
		}
		Class superClass = targetObject.getClass().getSuperclass();
		while (field == null && superClass != null) {
			try {
				field = superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				superClass = superClass.getSuperclass();
			}
		}
		if (field == null) {
			return false;
		}
		field.setAccessible(true);
		try {
			field.set(targetObject, fieldValue);
			return true;
		} catch (IllegalAccessException e) {
			return false;
		}
	}

	private <T extends Serializable> void applyDifferencesOnObject(Diff<?> diff, T copy, int depth) throws InvocationTargetException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Diff.ChangeLog> changeLogs = diff.getChangeLogs();
		int level = depth;
		// in case copy is set to null
		T referenceCopy = (T) BeanUtils.cloneBean(copy);
		for (Diff.ChangeLog changeLog : changeLogs) {
			if (changeLog.getDepth() > depth) {
				level = changeLog.getDepth();
			}
			if (changeLog.getDepth() == depth) {
				switch (changeLog.getStatus()) {
				case Delete:
					if (changeLog.getFieldName().equals(copy.getClass().getSimpleName())) {
						copy = null;
						break;
					} else {
						setField(copy, changeLog.getFieldName(), changeLog.getValue());
						break;
					}
				case Create:
					if(copy == null)
						copy = referenceCopy;
					setField(copy, changeLog.getFieldName(), changeLog.getValue());
				case Update:
					setField(copy, changeLog.getFieldName(), changeLog.getValue());
					break;
				default:
					break;
				}
			}
			if (depth < level) {
				Field[] fields = copy.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (field.getType().getName().equals(copy.getClass().getName())) {
						field.setAccessible(true);
						T object = (T) field.get(copy);
						if (object == null) {
							Class c = copy.getClass().getClassLoader().loadClass(copy.getClass().getName());
							object = (T) c.newInstance();
							setField(copy, field.getName(), object);
						}
						applyDifferencesOnObject(diff, copy, depth + 1);
					}
				}
			}
		}

	}

}