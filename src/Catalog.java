package src;

import java.io.File;

public class Catalog {

    public static boolean isExistFile(String pathToCatalog) {
        if (pathToCatalog == null) {
            return false;
        }
        File file = new File(pathToCatalog);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> T getNext_counter(typeCounter typeC, T last_value ) {
        if (last_value.getClass() == String.class) {
            char chars[] = ((String)last_value).toCharArray();
            chars[chars.length - 1]++;

            last_value = (T) String.valueOf(chars);
        } else if (last_value.getClass() == Integer.class) {
            Integer value =  ((Integer) last_value).intValue();
            last_value = (T) (value++);
        } else if (last_value.getClass() == Character.class) {
            Character value =  ((Character) last_value).charValue();
            last_value = (T) (value++);
        }
        return last_value;
    }

    public static String getValueCounter(Settings.Counter counter) {
        String value = Catalog.getNext_counter(typeCounter.valueOf(counter.getType()), counter.getLastValue());
        return value;
    }
}

