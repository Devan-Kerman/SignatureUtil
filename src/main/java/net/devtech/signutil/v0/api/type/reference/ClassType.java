package net.devtech.signutil.v0.api.type.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.devtech.signutil.v0.api.TypeArgument;
import net.devtech.signutil.v0.api.MethodResultType;
import net.devtech.signutil.v0.api.type.BaseType;
import net.devtech.signutil.v0.api.type.VoidType;
import org.jetbrains.annotations.Nullable;

public final class ClassType extends ReferenceType {
	public static final Map<ClassType, MethodResultType> UNBOX;
	public static final Map<MethodResultType, ClassType> BOX;
	public static final ClassType OBJECT = create("Ljava/lang/Object;");
	public static final ClassType BYTE = create("Ljava/lang/Byte;");
	public static final ClassType CHARACTER = create("Ljava/lang/Character;");
	public static final ClassType DOUBLE = create("Ljava/lang/Double;");
	public static final ClassType FLOAT = create("Ljava/lang/Float;");
	public static final ClassType INTEGER = create("Ljava/lang/Integer;");
	public static final ClassType LONG = create("Ljava/lang/Long;");
	public static final ClassType SHORT = create("Ljava/lang/Short;");
	public static final ClassType BOOLEAN = create("Ljava/lang/Boolean;");
	public static final ClassType VOID = create("Ljava/lang/Void;");

	static {
		Map<ClassType, MethodResultType> typeMap = new HashMap<>();
		typeMap.put(BYTE, BaseType.BYTE);
		typeMap.put(CHARACTER, BaseType.BYTE);
		typeMap.put(DOUBLE, BaseType.BYTE);
		typeMap.put(FLOAT, BaseType.BYTE);
		typeMap.put(INTEGER, BaseType.BYTE);
		typeMap.put(LONG, BaseType.BYTE);
		typeMap.put(SHORT, BaseType.BYTE);
		typeMap.put(BOOLEAN, BaseType.BYTE);
		typeMap.put(VOID, VoidType.INSTANCE);
		UNBOX = Collections.unmodifiableMap(typeMap);

		Map<MethodResultType, ClassType> reversed = new HashMap<>();
		typeMap.forEach((c, b) -> reversed.put(b, c));
		BOX = Collections.unmodifiableMap(reversed);
	}

	private ClassType parent;
	private String name, internalName;
	private List<TypeArgument> arguments;

	private ClassType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public static ClassType create(String buffer) {
		return create(buffer, 0, buffer.length());
	}

	public static ClassType create(String buffer, int start, int end) {
		return new ClassType(buffer, start, end);
	}

	private static void appendTypeArgs(StringBuilder builder, List<TypeArgument> arguments) {
		if (!arguments.isEmpty()) {
			builder.append('<');
			for (TypeArgument argument : arguments) {
				builder.append(argument);
			}
			builder.append('>');
		}
	}

	/**
	 * @return the name of the class, if the class is an inner class the name is the full name of the class
	 */
	public String getName() {
		this.parse();
		return this.name;
	}

	private static void write(StringBuilder builder, ClassType parent, String name, List<TypeArgument> arguments, boolean end) {
		if (parent != null) {
			write(builder, parent.getParent(), parent.getName(), parent.getArguments(), false);
			builder.append('.');
		} else {
			builder.append('L');
		}
		builder.append(name);
		appendTypeArgs(builder, arguments);
		if (end) {
			builder.append(';');
		}
	}

	public static ClassType create(@Nullable ClassType parent, String name, List<TypeArgument> arguments) {
		return create(parent, name, null, arguments);
	}

	public static ClassType create(@Nullable ClassType parent, String name, String internalName, List<TypeArgument> arguments) {
		StringBuilder builder = new StringBuilder(name.length() + arguments.size() * 15);
		write(builder, parent, name, arguments, true);
		ClassType type = ClassType.create(builder.toString());
		type.name = name;
		type.parent = parent;
		type.arguments = Collections.unmodifiableList(arguments);
		type.internalName = internalName;
		return type;
	}

	/**
	 * @return a new class type with the given type arguments
	 */
	public ClassType withArguments(List<TypeArgument> arguments) {
		return create(this.getParent(), this.getName(), this.getInternalName(), arguments);
	}

	/**
	 * the parent's buffer
	 */
	public ClassType withParent(@Nullable ClassType parent) {
		return create(parent, this.getName(), null, this.getArguments());
	}

	public ClassType withName(String name) {
		return create(this.getParent(), name, null, this.getArguments());
	}

	/**
	 * @return an unmodifiable list of the type arguments
	 */
	public List<TypeArgument> getArguments() {
		this.parse();
		return this.arguments;
	}

	void parse() {
		if (this.name != null) {
			return;
		}

		if (this.buffer.charAt(this.start) != 'L') {
			throw new IllegalArgumentException("class desc must start with 'L', found: " + this.buffer.substring(this.start, this.end));
		}

		int nameEnd = -1, nameStart = this.start + 1;
		List<TypeArgument> args = null;
		for (int i = this.start; i < this.end; i++) {
			char at = this.buffer.charAt(i);
			// nameEnd == -1 if we are reading a name

			if ((at == '<' || at == ';') && nameEnd == -1) {
				nameEnd = i;
			} else if (nameEnd != -1) {
				if (at == '.') {
					// this type is an inner class type, the parent has a different name and everything
					StringBuilder parentBuffer = new StringBuilder(2 + (i - nameStart));
					parentBuffer.append('L');
					parentBuffer.append(this.buffer, nameStart, i);
					parentBuffer.append(';');
					ClassType parent = ClassType.create(parentBuffer.toString());
					parent.parent = this.parent;
					parent.name = this.buffer.substring(nameStart, nameEnd);
					parent.arguments = args;
					this.parent = parent;
					args = null;
					// new name!
					nameEnd = -1;
					nameStart = i + 1;
				} else if (at != '>' && at != ';') {
					if (args == null) {
						args = new ArrayList<>();
					}
					TypeArgument argument = TypeArgument.read(this.buffer, i);
					i += argument.length() - 1;
					args.add(argument);
				}
			}
		}
		this.arguments = args == null ? Collections.emptyList() : Collections.unmodifiableList(args);
		this.name = this.buffer.substring(nameStart, nameEnd);
	}

	@Nullable
	public ClassType getParent() {
		this.parse();
		return this.parent;
	}

	public String getInternalName() {
		if (this.internalName == null) {
			ClassType parent = this.getParent();
			if (parent == null) {
				return this.internalName = this.getName();
			} else {
				return this.internalName = parent.getInternalName() + '$' + this.getName();
			}
		}
		return this.internalName;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.parent != null ? this.parent.hashCode() : 0);
		result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
		result = 31 * result + (this.internalName != null ? this.internalName.hashCode() : 0);
		result = 31 * result + (this.arguments != null ? this.arguments.hashCode() : 0);
		return result;
	}
}
