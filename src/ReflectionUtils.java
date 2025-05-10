import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void trimStringProperties(Object object) throws Exception {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class) {
                String getterMethodName = "get" + capitalizeFirstLetter(field.getName());

                String setterMethodName = "set" + capitalizeFirstLetter(field.getName());

                try {
                    Method getterMethod = clazz.getMethod(getterMethodName);

                    String value = (String) getterMethod.invoke(object);

                    if (value == null) {
                        value = "";
                    }

                    Method setterMethod = clazz.getMethod(setterMethodName, String.class);

                    setterMethod.invoke(object, value.trim());

                } catch (NoSuchMethodException e) {
                    System.out.println("Пропуск поля: " + field.getName() + ", не знайдено геттер або сеттер.");
                }
            }
        }
    }

    private static String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}