package org.apache.commons.collections4.functors;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;


public class InvokerTransformer<I, O> implements Transformer<I, O>, Serializable {


    private static final long serialVersionUID = -8653385846894047688L;


    private final String iMethodName;

    private final Class<?>[] iParamTypes;

    private final Object[] iArgs;


    public InvokerTransformer(final String methodName, final Class<?>[] paramTypes, final Object[] args) {
        super();
        iMethodName = methodName;
        iParamTypes = paramTypes != null ? paramTypes.clone() : null;
        iArgs = args != null ? args.clone() : null;
    }


    @Override
    public O transform(I input) {
        return null;
    }
}

