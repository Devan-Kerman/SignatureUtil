package net.devtech.signutil.v0.api.type;

import net.devtech.signutil.v0.api.MethodResultType;
import net.devtech.signutil.v0.api.Type;
import net.devtech.signutil.v0.api.type.reference.ReferenceType;

public abstract class JavaType extends Type implements MethodResultType {
	public JavaType(String buffer) {
		super(buffer);
	}

	public JavaType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static JavaType parse(String buffer, int start, int end) {
		char first = buffer.charAt(start);
		switch (first) {
		case '[':
		case 'L':
		case 'T':
			return ReferenceType.parse(buffer, start, end);
		default:
			return BaseType.parse(buffer, start, end);
		}
	}

	public static JavaType read(String buffer, int start) {
		char first = buffer.charAt(start);
		switch (first) {
		case '[':
		case 'L':
		case 'T':
			return ReferenceType.read(buffer, start);
		default:
			return BaseType.parse(buffer, start, start + 1);
		}
	}
}
