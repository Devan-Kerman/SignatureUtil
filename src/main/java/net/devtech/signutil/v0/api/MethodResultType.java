package net.devtech.signutil.v0.api;

import java.lang.reflect.Type;

import net.devtech.signutil.internal.TypeInternal;
import net.devtech.signutil.v0.api.bounded.TypeParameter;
import net.devtech.signutil.v0.api.type.BaseType;
import net.devtech.signutil.v0.api.type.JavaType;
import net.devtech.signutil.v0.api.type.VoidType;

/**
 * @see BaseType
 * @see VoidType
 */
public interface MethodResultType {
	static MethodResultType parse(Class<?> type) {
		StringBuilder builder = new StringBuilder();
		TypeInternal.appendDescriptor(type, builder, true);
		return parse(builder.toString());
	}

	/**
	 * if you want to parse TypeParameters {@link TypeParameter#create(Type)}
	 */
	static MethodResultType parse(Type type) {
		StringBuilder builder = new StringBuilder();
		TypeInternal.appendSignature(builder, type, true);
		return parse(builder.toString());
	}

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
