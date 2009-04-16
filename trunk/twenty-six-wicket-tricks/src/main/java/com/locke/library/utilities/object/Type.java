package com.locke.library.utilities.object;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.util.lang.Classes;

import com.locke.library.utilities.collections.MapList;

/**
 * @author jlocke
 */
public class Type<T>
{
    private static final Map<Class<?>, Type<?>> typeMap = new HashMap<Class<?>, Type<?>>();

    @SuppressWarnings("unchecked")
    public static <T> Type<T> forClass(final Class<T> javaType)
    {
        Type<T> type = (Type<T>)typeMap.get(javaType);
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
    private final Class<T> type;

    private Type(final Class<T> type)
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
        final String name = Classes.simpleName(this.type);
        final int dollarSign = name.lastIndexOf('$');
        if (dollarSign > 0)
        {
            return name.substring(dollarSign + 1);
        }
        return name;
    }
}
