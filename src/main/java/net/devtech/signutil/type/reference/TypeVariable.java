package net.devtech.signutil.type.reference;

/**
 * the use of a type parameter
 *
 * eg. TK;
 */
public final class TypeVariable extends ReferenceType {
	private String identifier;

	private TypeVariable(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static TypeVariable create(String buffer, int start, int end) {
		return new TypeVariable(buffer, start, end);
	}

	public static TypeVariable create(String buffer) {
		return new TypeVariable(buffer, 0, buffer.length());
	}

	public static TypeVariable createWithIdentifier(String name) {
		TypeVariable variable = create("T" + name + ";");
		variable.identifier = name;
		return variable;
	}

	public String getIdentifier() {
		if (this.identifier == null) {
			this.identifier = this.buffer.substring(this.start + 1, this.end - 1);
		}
		return this.identifier;
	}
}
