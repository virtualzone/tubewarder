package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class OutputHandlerConfigurationModelResolver implements TypeIdResolver {
    private JavaType baseType = null;

    @Override
    public void init(JavaType javaType) {
        baseType = javaType;
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        String id = "";
        if (aClass.equals(SysoutOutputHandlerConfigurationModel.class)) {
            id = "SYSOUT";
        } else if (aClass.equals(EmailOutputHandlerConfigurationModel.class)) {
            id = "EMAIL";
        }
        return id;
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, baseType.getRawClass());
    }

    @Override
    public JavaType typeFromId(String s) {
        return typeFromId(null, s);
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String s) {
        JavaType type;
        Class<?> clazz = null;
        if ("SYSOUT".equals(s)) {
            clazz = SysoutOutputHandlerConfigurationModel.class;
        } else if ("EMAIL".equals(s)) {
            clazz = EmailOutputHandlerConfigurationModel.class;
        }
        type = TypeFactory.defaultInstance().constructSpecializedType(baseType, clazz);
        return type;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
