package src;


public enum typeCounter {
    Text("Text"), Number("Number");

    private String value;
    typeCounter(String value) {
        if (value == null) {

        }
        this.value = value;
    }

    String getValue() {
        return value;
    }

    public static final String list[] = new String[]{"Text", "Number"};
    public static String[] getValues() {
        return list;
    }
}
