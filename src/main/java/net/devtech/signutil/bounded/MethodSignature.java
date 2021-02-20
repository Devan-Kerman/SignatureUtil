package net.devtech.signutil.bounded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.devtech.signutil.MethodResultType;
import net.devtech.signutil.type.JavaType;

public final class MethodSignature extends BoundedType {
	private List<JavaType> parameters;
	private MethodResultType returnType;

	public static MethodSignature create(String buffer, int start, int end) {
		return new MethodSignature(buffer, start, end);
	}

	public static MethodSignature create(String buffer) {
		return new MethodSignature(buffer, 0, buffer.length());
	}

	public static MethodSignature create(List<TypeParameter> typeParameters, List<JavaType> parameters, MethodResultType returnType) {
		StringBuilder builder = new StringBuilder(typeParameters.size() * 15 + parameters.size() * 15 + returnType.length());
		write(builder, typeParameters);
		builder.append('(');
		for (JavaType parameter : parameters) {
			builder.append(parameter);
		}
		builder.append(')');
		builder.append(returnType);
		MethodSignature signature = new MethodSignature(builder.toString(), 0, builder.length());
		signature.typeParameters = typeParameters;
		signature.parameters = parameters;
		signature.returnType = returnType;
		return signature;
	}

	public MethodSignature withTypeParameters(List<TypeParameter> typeParameters) {
		return create(typeParameters, this.getParameters(), this.getReturnType());
	}

	public MethodSignature withParameters(List<JavaType> parameters) {
		return create(this.getTypeParameters(), parameters, this.getReturnType());
	}

	public MethodSignature withReturnType(MethodResultType returnType) {
		return create(this.getTypeParameters(), this.getParameters(), returnType);
	}

	private MethodSignature(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public List<JavaType> getParameters() {
		if (this.parameters == null) {
			// compute index
			this.getTypeParameters();
			if (this.buffer.charAt(this.index) != '(') {
				throw new IllegalStateException("Invalid Method Signature: " + this.buffer.substring(this.start, this.end));
			}
			this.index++;
			List<JavaType> types = null;

			while (this.buffer.charAt(this.index) != ')') {
				JavaType type = JavaType.read(this.buffer, this.index);
				if (types == null) {
					types = new ArrayList<>();
				}
				types.add(type);
				this.index += type.length();
			}
			this.index++;

			if (types == null) {
				this.parameters = Collections.emptyList();
			} else {
				this.parameters = Collections.unmodifiableList(types);
			}
		}
		return this.parameters;
	}

	public MethodResultType getReturnType() {
		if(this.returnType == null) {
			// compute index
			this.getParameters();
			this.returnType = MethodResultType.parse(this.buffer, this.index, this.end);
		}
		return this.returnType;
	}
}
