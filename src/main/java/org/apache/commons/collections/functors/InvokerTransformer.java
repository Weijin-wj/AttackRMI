package org.apache.commons.collections.functors;

import org.apache.commons.collections.Transformer;

import java.io.Serializable;

public class InvokerTransformer implements Transformer, Serializable {
    static final long serialVersionUID = -8653385846894047688L;
    private final String iMethodName;
    private final Class[] iParamTypes;
    private final Object[] iArgs;

    public InvokerTransformer(String methodName, Class[] paramTypes, Object[] args) {
        this.iMethodName = methodName;
        this.iParamTypes = paramTypes;
        this.iArgs = args;
    }

    @Override
    public Object transform(Object var1) {
        return null;
    }
}
