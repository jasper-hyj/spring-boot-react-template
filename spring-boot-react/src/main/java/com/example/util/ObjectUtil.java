package com.example.util;

import java.lang.reflect.Field;

import com.example.model.Model;

public class ObjectUtil {

    /**
     * Updates the fields of the target object with the non-null values from the
     * update request object.
     *
     * @param target        The target object to be updated
     * @param updateRequest The update request object containing the new values
     * @param <T>           The type of the target object
     * @return The updated target object
     */
    public static <T extends Model<T>> T update(T target, Object updateRequest) {
        T updatedTarget = target.clone();
        // Get the class of the update request object
        Class<?> updateRequestClass = updateRequest.getClass();
        // Get all declared fields of the update request object
        Field[] fields = updateRequestClass.getDeclaredFields();

        // Iterate over each field in the update request object
        for (Field field : fields) {
            // Allow access to the field (even if it's private)
            field.setAccessible(true);
            // Get the value of the field from the update request object
            try {
                Object value = field.get(updateRequest);
                // If the value is not null, update the corresponding field in the target object
                if (value != null) {
                    // Get the field in the target object with the same name as the field in the
                    // update request object
                    try {
                        Field targetField = target.getClass().getDeclaredField(field.getName());
                        // Allow access to the target field
                        targetField.setAccessible(true);
                        // Set the value of the target field to the value from the update request object
                        targetField.set(updatedTarget, value);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // Return the updated target object
        return updatedTarget;
    }

}
