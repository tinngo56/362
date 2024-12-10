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
        
        // Loop through the class hierarchy until we hit Object class
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null) {
                        // Convert LocalDateTime to String when storing in map
                        if (value instanceof LocalDateTime) {
                            value = ((LocalDateTime) value).toString();
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
                            // Convert String to LocalDateTime
                            LocalDateTime dateTime = LocalDateTime.parse((String) value);
                            field.set(instance, dateTime);
                        } else if (value instanceof Number) {
                            Number numValue = (Number) value;
    
                            if (fieldType == Integer.class || fieldType == int.class) {
                                field.set(instance, numValue.intValue());
                            } else if (fieldType == Long.class || fieldType == long.class) {
                                field.set(instance, numValue.longValue());
                            } else if (fieldType == Double.class || fieldType == double.class) {
                                field.set(instance, numValue.doubleValue());
                            } else if (fieldType == Float.class || fieldType == float.class) {
                                field.set(instance, numValue.floatValue());
                            } else if (fieldType == Short.class || fieldType == short.class) {
                                field.set(instance, numValue.shortValue());
                            } else if (fieldType == Byte.class || fieldType == byte.class) {
                                field.set(instance, numValue.byteValue());
                            }
                        } else if (fieldType == String.class && !(value instanceof String)) {
                            field.set(instance, String.valueOf(value));
                        } else if(fieldType.isEnum()) {
                            field.set(instance, Enum.valueOf((Class<Enum>) fieldType, value.toString()));
                        } else {
                            field.set(instance, value);
                        }
                    }
                }
                
                currentClass = currentClass.getSuperclass();
            }
    
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error converting map to object", e);
        }
    }
}