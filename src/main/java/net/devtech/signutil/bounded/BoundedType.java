package net.devtech.signutil.bounded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.devtech.signutil.Type;

public class BoundedType extends Type {
	List<TypeParameter> typeParameters;
	protected int index;

	BoundedType(String buffer, int start, int end) {
		super(buffer, start, end);
	}

	public List<TypeParameter> getTypeParameters() {
		if (this.typeParameters == null) {
			if (this.buffer.charAt(this.start) == '<') {
				this.index = this.start + 1;
				List<TypeParameter> parameters = new ArrayList<>();
				while (this.buffer.charAt(this.index) != '>') {
					TypeParameter parameter = TypeParameter.read(this.buffer, this.index);
					parameters.add(parameter);
					this.index += parameter.length();
				}
				this.index++;
				this.typeParameters = Collections.unmodifiableList(parameters);
			} else {
				this.index = this.start;
				this.typeParameters = Collections.emptyList();
			}
		}
		return this.typeParameters;
	}

	protected static void write(StringBuilder buffer, List<TypeParameter> parameters) {
		if(!parameters.isEmpty()) {
			buffer.append('<');
			for (TypeParameter parameter : parameters) {
				buffer.append(parameter);
			}
			buffer.append('>');
		}
	}
}
