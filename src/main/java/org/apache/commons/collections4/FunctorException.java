package org.apache.commons.collections4;

public class FunctorException extends RuntimeException {


    private static final long serialVersionUID = -4704772662059351193L;


    public FunctorException() {
        super();
    }


    public FunctorException(final String msg) {
        super(msg);
    }


    public FunctorException(final Throwable rootCause) {
        super(rootCause);
    }


    public FunctorException(final String msg, final Throwable rootCause) {
        super(msg, rootCause);
    }

}
