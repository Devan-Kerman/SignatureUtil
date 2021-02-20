package net.devtech.signutil.v0.api.type.reference;

import net.devtech.signutil.v0.api.type.BaseType;
import net.devtech.signutil.v0.api.type.JavaType;

public final class ArrayType extends ReferenceType {
	public static final ArrayType BYTE_ARRAY_1 = withType(BaseType.BYTE);
	public static final ArrayType CHAR_ARRAY_1 = withType(BaseType.CHAR);
	public static final ArrayType DOUBLE_ARRAY_1 = withType(BaseType.DOUBLE);
	public static final ArrayType FLOAT_ARRAY_1 = withType(BaseType.FLOAT);
	public static final ArrayType INT_ARRAY_1 = withType(BaseType.INT);
	public static final ArrayType LONG_ARRAY_1 = withType(BaseType.LONG);
	public static final ArrayType SHORT_ARRAY_1 = withType(BaseType.SHORT);
	public static final ArrayType BOOLEAN_ARRAY_1 = withType(BaseType.BOOLEAN);
	public static final ArrayType OBJECT_ARRAY_1 = withType(ClassType.OBJECT);

	JavaType parsed;

	public static ArrayType create(String buffer, int start, int end) {
		return new ArrayType(buffer, start, end);
	}

	public static ArrayType create(String buffer) {
		return create(buffer, 0, buffer.length());
	}

	private ArrayType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public JavaType getComponentType() {
		if(this.parsed == null) {
			this.parsed = JavaType.parse(this.buffer, this.start + 1, this.end);
		}
		return this.parsed;
	}

	public static ArrayType withType(JavaType type) {
		String buffer = "[" + type;
		ArrayType t = new ArrayType(buffer, 0, buffer.length());
		t.parsed = type;
		return t;
	}
}
