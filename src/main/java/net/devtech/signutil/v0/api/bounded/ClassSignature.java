package net.devtech.signutil.v0.api.bounded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.devtech.signutil.v0.api.type.reference.ClassType;
import net.devtech.signutil.v0.api.type.reference.ReferenceType;

/**
 * a class's signature
 */
public final class ClassSignature extends BoundedType {
	private ClassType superClass;
	private List<ClassType> interfaces;

	public static ClassSignature create(String buffer) {
		return new ClassSignature(buffer, 0, buffer.length());
	}

	public static ClassSignature create(String buffer, int start, int end) {
		return new ClassSignature(buffer, start, end);
	}

	private ClassSignature(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public ClassSignature withTypeParameters(List<TypeParameter> parameters) {
		return create(parameters, this.getSuperClass(), this.getInterfaces());
	}

	public ClassSignature withSuperClass(ClassType superClass) {
		return create(this.getTypeParameters(), superClass, this.getInterfaces());
	}

	public ClassSignature withInterfaces(List<ClassType> interfaces) {
		return create(this.getTypeParameters(), this.getSuperClass(), interfaces);
	}

	public static ClassSignature create(List<TypeParameter> parameters, ClassType superClass, List<ClassType> interfaces) {
		StringBuilder buffer = new StringBuilder();
		write(buffer, parameters);
		buffer.append(superClass);
		for (ClassType anInterface : interfaces) {
			buffer.append(anInterface);
		}
		ClassSignature signature = new ClassSignature(buffer.toString(), 0, buffer.length());
		signature.typeParameters = parameters;
		signature.superClass = superClass;
		signature.interfaces = interfaces;
		return signature;
	}

	public ClassType getSuperClass() {
		if(this.superClass == null) {
			// compute index
			this.getTypeParameters();
			ClassType parsed = this.superClass = (ClassType) ReferenceType.read(this.buffer, this.index);
			this.index += parsed.length();
		}
		return this.superClass;
	}

	public List<ClassType> getInterfaces() {
		if(this.interfaces == null) {
			// compute index
			this.getSuperClass();
			List<ClassType> types = new ArrayList<>();
			while (this.index < this.end) {
				ClassType type = (ClassType) ReferenceType.read(this.buffer, this.index);
				this.index += type.length();
				types.add(type);
			}
			this.interfaces = Collections.unmodifiableList(types);
		}
		return this.interfaces;
	}
}
