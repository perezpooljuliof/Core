package mx.com.core.db;

import mx.com.core.db.param.ParamAccess;

public class ParameterBean {
    private String name;
    private Object value;
    private int type;
    private String typeName;
    private ParamAccess access;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ParamAccess getAccess() {
        return access;
    }

    public void setAccess(ParamAccess access) {
        this.access = access;
    }
}
