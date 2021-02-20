package net.devtech.signutil.type.reference;

import net.devtech.signutil.type.JavaType;

public abstract class ReferenceType extends JavaType {
	ReferenceType(String buffer) {
		super(buffer);
	}

	ReferenceType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static ReferenceType read(String buffer, int start) {
		char s = buffer.charAt(start);
		switch (s) {
		case 'L':
			int open = 0;
			for (int i = start; i < buffer.length(); i++) {
				char at = buffer.charAt(i);
				if (at == '<') {
					open++;
				} else if (at == '>') {
					open--;
				} else if(at == ';' && open == 0) {
					return ClassType.create(buffer, start, i+1);
				}
			}
			break;
		case 'T':
			return TypeVariable.create(buffer, start, buffer.indexOf(';', start) + 1);
		case '[':
			JavaType signature = JavaType.read(buffer, start + 1);
			//noinspection StringEquality
			if (buffer == signature.buffer) {
				ArrayType type = ArrayType.create(buffer, start, signature.end);
				type.parsed = signature;
				return type;
			} else {
				return ArrayType.withType(signature);
			}
		}
		throw new IllegalArgumentException("Invalid ReferenceTypeSignature: " + buffer.substring(start));
	}

	public static ReferenceType parse(String buffer, int start, int end) {
		char s = buffer.charAt(start);
		switch (s) {
		case 'L':
			return ClassType.create(buffer, start, end);
		case '[':
			return ArrayType.create(buffer, start, end);
		case 'T':
			return TypeVariable.create(buffer, start, end);
		}
		throw new IllegalArgumentException("Unknown ReferenceTypeSignature");
	}
}
