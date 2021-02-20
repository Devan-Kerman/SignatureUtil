package net.devtech.signutil;

import net.devtech.signutil.type.BaseType;
import net.devtech.signutil.type.JavaType;
import net.devtech.signutil.type.VoidType;

/**
 * @see BaseType
 * @see VoidType
 */
public interface MethodResultType {
	// len is always one
	static MethodResultType parse(String buffer, int start, int end) {
		char s = buffer.charAt(start);
		if(s == 'V') {
			return VoidType.INSTANCE;
		} else {
			return JavaType.parse(buffer, start, end);
		}
	}

	static MethodResultType parse(String buffer) {
		return parse(buffer, 0, buffer.length());
	}

	default String getDesc() {
		return this.toString();
	}

	int length();
}
