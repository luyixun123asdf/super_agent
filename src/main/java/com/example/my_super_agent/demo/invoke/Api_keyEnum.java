package com.example.my_super_agent.demo.invoke;

public enum Api_keyEnum {

    DASH_SCOPE_API_KEY("sk-b568b9380df046618646eef63ee9997f");
    static final String DASH_SCOPE_API_KEY1 = "sk-b568b9380df046618646eef63ee9997f";


    private  final String value;

    Api_keyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByName(String name) {
        for (Api_keyEnum value : Api_keyEnum.values()) {
            if (value.name().equals(name)) {
                return value.getValue();
            }
        }
        return null;
    }


}
