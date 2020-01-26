package mx.com.core.utilidades;

import mx.com.core.db.ParameterBean;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapSqlParameterSourceImpl extends MapSqlParameterSource {
    private final Map<String, Object> objects = new LinkedHashMap();

    public void addValue(ParameterBean parameterBean) {
        this.addValue(parameterBean.getName(), parameterBean.getValue());
        objects.put(parameterBean.getName(), parameterBean);
    }

    public Object getParameter(String parameterName) {
        return objects.get(parameterName);
    }
}
