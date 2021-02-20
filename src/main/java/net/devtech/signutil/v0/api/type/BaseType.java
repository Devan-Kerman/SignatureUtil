package net.devtech.signutil.v0.api.type;

public final class BaseType extends JavaType {
	static final String BASE_BUFFER = "BCDFIJSZ";
	public static final BaseType BYTE = new BaseType(BASE_BUFFER, 0, 1);
	public static final BaseType CHAR = new BaseType(BASE_BUFFER, 1, 2);
	public static final BaseType DOUBLE = new BaseType(BASE_BUFFER, 2, 3);
	public static final BaseType FLOAT = new BaseType(BASE_BUFFER, 3, 4);
	public static final BaseType INT = new BaseType(BASE_BUFFER, 4, 5);
	public static final BaseType LONG = new BaseType(BASE_BUFFER, 5, 6);
	public static final BaseType SHORT = new BaseType(BASE_BUFFER, 6, 7);
	public static final BaseType BOOLEAN = new BaseType(BASE_BUFFER, 7, 8);
	// matches the indexes in BASE_BUFFER
	static final BaseType[] ORDERED = {
			BYTE,
			CHAR,
			DOUBLE,
			FLOAT,
			INT,
			LONG,
			SHORT,
			BOOLEAN
	};

	private BaseType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static BaseType parse(char c) {
		return ORDERED[BASE_BUFFER.indexOf(c)];
	}

	public static BaseType parse(String buffer) {
		return parse(buffer, 0, buffer.length());
	}

	public static BaseType parse(String buffer, int start, int end) {
		if (end - start != 1) {
			throw new IllegalArgumentException("Invalid BaseType Signature! " + buffer.substring(start, end));
		}
		return parse(buffer.charAt(start));
	}
}
