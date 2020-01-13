package mx.com.core.db.param;

public enum ParamType {
    INT(1, "INT"), VARCHAR(2, "VARCHAR"), DATE(3, "DATE"),
    DATETIME(4,"DATETIME"), DOUBLE(5, "DOUBLE"), BIGINT(7, "BIGINT");

    ParamType(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    String name;
    int type;
}