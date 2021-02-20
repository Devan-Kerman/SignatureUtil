package net.devtech.signutil.v0.api.type;

import net.devtech.signutil.v0.api.Type;
import net.devtech.signutil.v0.api.MethodResultType;

public final class VoidType extends Type implements MethodResultType {
	public static final VoidType INSTANCE = new VoidType("V");
	private VoidType(String buffer) {
		super(buffer);
	}
}
