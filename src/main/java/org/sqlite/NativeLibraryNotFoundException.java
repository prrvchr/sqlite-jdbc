package org.sqlite;

public class NativeLibraryNotFoundException extends Exception {
    static final long serialVersionUID = -7331480161413870991L;

	public NativeLibraryNotFoundException(String message) {
        super(message);
    }
}
