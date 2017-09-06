package org.opennms.jdhcp;

public class MalformedPacketException extends Exception {
    private static final long serialVersionUID = 1L;

    public MalformedPacketException() {
        super();
    }
    
    public MalformedPacketException(final String message) {
        super(message);
    }
    
    public MalformedPacketException(final Throwable cause) {
        super(cause);
    }
    
    public MalformedPacketException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
