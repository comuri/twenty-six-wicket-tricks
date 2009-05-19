package com.locke.library.utilities.object;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.Strings;

import com.locke.library.utilities.collections.MapList;

/**
 * @author jlocke
 */
public class Type
{
    private static final Map<Class<?>, Type> typeMap = new HashMap<Class<?>, Type>();

    @SuppressWarnings("unchecked")
    public static Type forClass(final Class javaType)
    {
        Type type = typeMap.get(javaType);
        if (type == null)
        {
            type = new Type(javaType);
            typeMap.put(javaType, type);
        }
        return type;
    }

    private final List<Method> methods = new ArrayList<Method>();
    private final MapList<Class<? extends Annotation>, Method> methodsForAnnotation =
            new MapList<Class<? extends Annotation>, Method>();
    private final Class<?> type;

    private Type(final Class<?> type)
    {
        this.type = type;
    }

    public <A extends Annotation> Iterable<Method> annotatedMethods(final Class<A> annotationType)
    {
        List<Method> annotatedMethods = this.methodsForAnnotation.list(annotationType);
        if (annotatedMethods == null)
        {
            for (final Method method : methods())
            {
                if (method.isAnnotationPresent(annotationType))
                {
                    this.methodsForAnnotation.add(annotationType, method);
                }
            }
            annotatedMethods = this.methodsForAnnotation.list(annotationType);
            if (annotatedMethods == null)
            {
                this.methodsForAnnotation.emptyList(annotationType);
                annotatedMethods = this.methodsForAnnotation.list(annotationType);
            }
        }
        return annotatedMethods;
    }

    public boolean isPrimitive()
    {
        return this.type.isPrimitive();
    }

    public Iterable<Method> methods()
    {
        if (this.methods.isEmpty())
        {
            Class<?> at = this.type;
            do
            {
                for (final Method method : at.getDeclaredMethods())
                {
                    this.methods.add(method);
                }
                at = at.getSuperclass();
            }
            while (at != Object.class);
        }
        return this.methods;
    }

    public String simpleName()
    {
        final String name = Classes.simpleName(this.type).replace('$', '.');
        final String end = Strings.lastPathComponent(name, '.');
        if (end.matches("\\d+"))
        {
            return Type.forClass(this.type.getSuperclass()).simpleName();
        }
        return name;
    }
}
