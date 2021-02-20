package net.devtech.signutil.v0.api;

import net.devtech.signutil.v0.api.type.reference.ClassType;
import net.devtech.signutil.v0.api.type.reference.ReferenceType;
import org.jetbrains.annotations.Nullable;

// todo doesn't this have multiple bounds?
public final class TypeArgument extends Type {
	public static final TypeArgument WILD_CARD = new TypeArgument("*", 0, 1);
	private ReferenceType type;
	private char wildcard;

	private TypeArgument(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static TypeArgument createExtends(ReferenceType type) {
		return create(type, '+');
	}

	private static TypeArgument create(ReferenceType type, char wildcard) {
		String buffer = wildcard + type.buffer.substring(type.start, type.end);
		TypeArgument argument = create(buffer);
		argument.type = type;
		argument.wildcard = wildcard;
		return argument;
	}

	public static TypeArgument create(String buffer) {
		return create(buffer, 0, buffer.length());
	}

	public static TypeArgument create(String buffer, int start, int end) {
		if (buffer.charAt(start) == '*') {
			if (end != start + 1) {
				throw new IllegalArgumentException("Invalid type argument: " + buffer.substring(start, end));
			}
			return WILD_CARD;
		}
		return new TypeArgument(buffer, start, end);
	}

	public static TypeArgument createSuper(ReferenceType type) {
		return create(type, '-');
	}

	public static TypeArgument read(String buffer, int start) {
		char c = buffer.charAt(start);
		if (c == '*') {
			return WILD_CARD;
		}

		ReferenceType ref;
		if (c == '-' || c == '+') {
			ref = ReferenceType.read(buffer, start + 1);
		} else {
			ref = ReferenceType.read(buffer, start);
		}

		TypeArgument arg;
		//noinspection StringEquality
		if (ref.buffer == buffer) {
			arg = new TypeArgument(buffer, start, ref.end);
		} else {
			if (c == '-' || c == '+') {
				arg = new TypeArgument(c + ref.toString(), 0, ref.end + 1);
			} else {
				arg = new TypeArgument(ref.toString(), 0, ref.end);
			}
		}

		arg.type = ref;
		return arg;
	}

	public TypeArgument withWildcard(char wildcard) {
		if (wildcard == '*') {
			return WILD_CARD;
		}

		ReferenceType type = this.getType();
		if(type == null) {
			return create(ClassType.OBJECT, wildcard);
		} else if (wildcard == '=') {
			return createExact(type);
		} else if(wildcard == '+' || wildcard == '-'){
			return create(type, wildcard);
		}
		throw new IllegalArgumentException("Invalid wildcard '" + wildcard + "' (=*+-)");
	}

	public TypeArgument withType(ReferenceType type) {
		if (this.wildcard == '*' || this.wildcard == '=') {
			return createExact(type);
		} else {
			return create(type, this.wildcard);
		}
	}

	/**
	 * @return null if {@link #getWildcard()} == '*'
	 */
	@Nullable
	public ReferenceType getType() {
		if (this.type != null) {
			return this.type;
		}

		char wildcard = this.getWildcard();
		if (wildcard == '*') {
			return null;
		} else if (wildcard == '=') {
			return this.type = ReferenceType.parse(this.buffer, this.start, this.end);
		} else {
			return this.type = ReferenceType.parse(this.buffer, this.start + 1, this.end);
		}
	}

	public static TypeArgument createExact(ReferenceType type) {
		TypeArgument argument = new TypeArgument(type.buffer, type.start, type.end);
		argument.type = type;
		argument.wildcard = '=';
		return argument;
	}

	/**
	 * @return '=' if no wildcard
	 */
	public char getWildcard() {
		if (this.wildcard == 0) {
			char start = this.buffer.charAt(this.start);
			int offset;
			if (start == '*') {
				this.wildcard = '*';
				offset = -1;
			} else if (start == '+' || start == '-') {
				this.wildcard = start;
				offset = 1;
			} else {
				this.wildcard = '=';
				offset = 0;
			}

			if (offset != -1) {
				this.type = ReferenceType.parse(this.buffer, this.start + offset, this.end);
			}
		}
		return this.wildcard;
	}
}
