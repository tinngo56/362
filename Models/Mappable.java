package Models;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
                        map.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error converting object to map", e);
            }
            
            // Move up to the superclass
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
            // Get the actual class of T
            Class<T> clazz = (Class<T>) this.getClass();
    
            // Create a new instance using the default constructor
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
    
            // Start with current class and work up the hierarchy
            Class<?> currentClass = clazz;
            while (currentClass != null && currentClass != Object.class) {
                // Get fields for current class level
                Field[] fields = currentClass.getDeclaredFields();
    
                // Set each field from the map
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = map.get(field.getName());
    
                    if (value != null) {
                        // Handle primitive type conversions
                        Class<?> fieldType = field.getType();
    
                        if (value instanceof Number) {
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