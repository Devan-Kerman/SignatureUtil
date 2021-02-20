package net.devtech.signutil;

public abstract class Type {
	public final String buffer;
	public final int start, end;
	int hashCode;

	public Type(String buffer) {
		this(buffer, 0, buffer.length());
	}

	public Type(String buffer, int start, int end) {
		this.buffer = buffer;
		this.start = start;
		this.end = end;
	}

	protected int computeHashCode() {
		int result = 1;
		for (int i = this.start; i < this.end; i++) {
			result = 31 * result + this.buffer.charAt(i);
		}
		return result;
	}

	@Override
	public int hashCode() {
		if(this.hashCode == -1) {
			this.hashCode = this.computeHashCode();
		}
		return this.hashCode;
	}

	public int length() {
		return this.end - this.start;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}

		Type type = (Type) o;
		int len = this.end - this.start;
		if (type.end - type.start == len) {
			for (int i = 0; i < len; i++) {
				if (type.buffer.charAt(i + type.start) != this.buffer.charAt(i + this.start)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.buffer.substring(this.start, this.end);
	}
}
