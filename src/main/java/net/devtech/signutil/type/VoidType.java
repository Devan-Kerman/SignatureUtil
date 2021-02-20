package net.devtech.signutil.type;

import net.devtech.signutil.Type;
import net.devtech.signutil.MethodResultType;

public final class VoidType extends Type implements MethodResultType {
	public static final VoidType INSTANCE = new VoidType("V");
	private VoidType(String buffer) {
		super(buffer);
	}
}
