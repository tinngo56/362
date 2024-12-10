package Models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class Mappable<T> {
    /**
     * Convert the object to a Map representation using reflection
     * 
     * @return Map containing the object's properties
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Class<?> currentClass = this.getClass();
        
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null) {
                        if (value instanceof LocalDateTime) {
                            value = ((LocalDateTime) value).toString();
                        } else if (value instanceof Mappable) {
                            // Handle nested Mappable objects
                            value = ((Mappable<?>) value).toMap();
                        }
                        map.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error converting object to map", e);
            }
            
            currentClass = currentClass.getSuperclass();
        }
        
        return map;
    }

    /**
     * Convert a Map back to an object of type T using reflection
     * 
     * @param map The map containing the object's properties
     * @return An instance of type T
     */
    @SuppressWarnings("unchecked")
    public T fromMap(Map<String, Object> map) {
        try {
            Class<T> clazz = (Class<T>) this.getClass();
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
    
            Class<?> currentClass = clazz;
            while (currentClass != null && currentClass != Object.class) {
                Field[] fields = currentClass.getDeclaredFields();
    
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = map.get(field.getName());
    
                    if (value != null) {
                        Class<?> fieldType = field.getType();
    
                        if (fieldType == LocalDateTime.class && value instanceof String) {
                            field.set(instance, LocalDateTime.parse((String) value));
                        } else if (value instanceof Map && Mappable.class.isAssignableFrom(fieldType)) {
                            // Handle nested Mappable objects
                            Constructor<?> fieldConstructor = fieldType.getDeclaredConstructor();
                            fieldConstructor.setAccessible(true);
                            Mappable<?> nestedInstance = (Mappable<?>) fieldConstructor.newInstance();
                            field.set(instance, nestedInstance.fromMap((Map<String, Object>) value));
                        } else if (value instanceof Number) {
                            setNumberField(field, instance, (Number) value);
                        } else if (fieldType == String.class && !(value instanceof String)) {
                            field.set(instance, String.valueOf(value));
                        } else if(fieldType.isEnum()) {
                            field.set(instance, Enum.valueOf((Class<Enum>) fieldType, value.toString()));
                        } else if (fieldType.isAssignableFrom(value.getClass())) {
                            field.set(instance, value);
                        }
                    }
                }
                
                currentClass = currentClass.getSuperclass();
            }
    
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error converting map to object: " + e.getMessage(), e);
        }
    }

    private void setNumberField(Field field, Object instance, Number value) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (fieldType == Integer.class || fieldType == int.class) {
            field.set(instance, value.intValue());
        } else if (fieldType == Long.class || fieldType == long.class) {
            field.set(instance, value.longValue());
        } else if (fieldType == Double.class || fieldType == double.class) {
            field.set(instance, value.doubleValue());
        } else if (fieldType == Float.class || fieldType == float.class) {
            field.set(instance, value.floatValue());
        } else if (fieldType == Short.class || fieldType == short.class) {
            field.set(instance, value.shortValue());
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            field.set(instance, value.byteValue());
        }
    }
}