package net.devtech.signutil.bounded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.devtech.signutil.Type;
import net.devtech.signutil.type.reference.ClassType;
import net.devtech.signutil.type.reference.ReferenceType;

public final class TypeParameter extends Type {
	// only valid for read type parameters
	private int index = -1;

	private String identifier;
	private ReferenceType classBound;
	private List<ReferenceType> interfaceBound;

	public static TypeParameter create(String buffer, int start, int end) {
		return new TypeParameter(buffer, start, end);
	}

	public static TypeParameter create(String buffer) {
		return create(buffer, 0, buffer.length());
	}

	public static TypeParameter read(String buffer, int start) {
		TypeParameter parameter = create(buffer, start, buffer.length());
		// compute indexes
		parameter.getInterfaceBounds();
		TypeParameter copied = new TypeParameter(buffer, start, parameter.index);
		copied.identifier = parameter.getIdentifier();
		copied.classBound = parameter.getClassBound();
		copied.interfaceBound = parameter.getInterfaceBounds();
		return copied;
	}

	private TypeParameter(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public TypeParameter withIdentifier(String identifier) {
		return create(identifier, this.classBound, this.interfaceBound);
	}

	public TypeParameter withClassBound(ReferenceType classBound) {
		return create(this.identifier, classBound, this.interfaceBound);
	}

	public TypeParameter withInterfaceBounds(List<ReferenceType> interfaceBounds) {
		return create(this.identifier, this.classBound, Collections.unmodifiableList(interfaceBounds));
	}

	public static TypeParameter create(String identifier, ReferenceType classBound, List<ReferenceType> interfaceBound) {
		StringBuilder builder = new StringBuilder(identifier.length() + classBound.length() + interfaceBound.size() * 15);
		builder.append(identifier).append(':');
		if(classBound != ClassType.OBJECT || interfaceBound.isEmpty()) {
			builder.append(classBound);
		}
		for (ReferenceType type : interfaceBound) {
			builder.append(':').append(type);
		}

		TypeParameter parameter = create(builder.toString());
		parameter.identifier = identifier;
		parameter.classBound = classBound;
		parameter.interfaceBound = interfaceBound;
		return parameter;
	}

	public List<ReferenceType> getInterfaceBounds() {
		if (this.interfaceBound == null) {
			// compute index
			this.getClassBound();
			List<ReferenceType> types = null;
			while (this.index < this.end && this.buffer.charAt(this.index) == ':') {
				ReferenceType type = ReferenceType.read(this.buffer, this.index + 1);
				if (types == null) {
					types = new ArrayList<>();
				}
				types.add(type);
				this.index += type.length() + 1;
			}

			if (types == null) {
				this.interfaceBound = Collections.emptyList();
			} else {
				this.interfaceBound = Collections.unmodifiableList(types);
			}
		}
		return this.interfaceBound;
	}

	public ReferenceType getClassBound() {
		if (this.classBound == null) {
			// compute index
			this.getIdentifier();

			// if bit after starts interface bound immediately, then there was no classBound
			if (this.buffer.charAt(this.index + 1) == ':') {
				this.classBound = ClassType.OBJECT;
				this.index++;
			} else {
				this.index += (this.classBound = ReferenceType.read(this.buffer, this.index + 1)).length() + 1;
			}
		}
		return this.classBound;
	}

	public String getIdentifier() {
		if (this.identifier == null) {
			int index = this.buffer.indexOf(':', this.start);
			this.identifier = this.buffer.substring(this.start, index);
			this.index = index;
		}
		return this.identifier;
	}

	@Override
	protected int computeHashCode() {
		int result = 1;
		result = 31*result + Objects.hashCode(this.getIdentifier());
		result = 31*result + Objects.hashCode(this.getClassBound());
		result = 31*result + Objects.hashCode(this.getInterfaceBounds());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TypeParameter)) {
			return false;
		}

		TypeParameter parameter = (TypeParameter) o;
		if (!Objects.equals(this.getIdentifier(), parameter.getIdentifier())) {
			return false;
		}
		if (!Objects.equals(this.getClassBound(), parameter.getClassBound())) {
			return false;
		}
		return Objects.equals(this.getInterfaceBounds(), parameter.getInterfaceBounds());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(this.getIdentifier());
		result = 31 * result + Objects.hashCode(this.getClassBound());
		result = 31 * result + Objects.hashCode(this.getInterfaceBounds());
		return result;
	}
}
