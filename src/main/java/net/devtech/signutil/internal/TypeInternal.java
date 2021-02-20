package net.devtech.signutil.internal;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInternal {
	public static void appendSignature(StringBuilder builder, java.lang.reflect.Type type, boolean visitEnd) {
		if (type instanceof Class) {
			Class<?> c = (Class)type;
			appendDescriptor(c, builder, visitEnd);
		} else if (type instanceof GenericArrayType) {
			builder.append('[');
			appendSignature(builder, ((GenericArrayType)type).getGenericComponentType(), visitEnd);
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)type;
			java.lang.reflect.Type owner = pt.getOwnerType();
			Class<?> raw = (Class)pt.getRawType();
			if (owner != null && !(owner instanceof Class)) {
				appendSignature(builder, owner, false);
				builder.append('.');
				builder.append(raw.getSimpleName());
			} else {
				appendSignature(builder, raw, false);
			}

			java.lang.reflect.Type[] args = pt.getActualTypeArguments();
			if(args.length != 0) {
				builder.append('<');
				for (java.lang.reflect.Type arg : args) {
					appendSignature(builder, arg, true);
				}
				builder.append('>');
			}

			if (visitEnd) {
				builder.append(';');
			}

		} else if (type instanceof TypeVariable) {
			builder.append('T').append(((TypeVariable)type).getName()).append(';');
		} else if (type instanceof WildcardType) {
			WildcardType wt = (WildcardType)type;
			java.lang.reflect.Type[] array = wt.getLowerBounds();
			if (array.length > 0) {
				builder.append('-');
			} else {
				array = wt.getUpperBounds();
				if (array.length == 1 && array[0] == Object.class) {
					builder.append('*');
				} else {
					builder.append('+');
				}
			}

			java.lang.reflect.Type[] var7 = array;
			int var8 = array.length;

			for(int var9 = 0; var9 < var8; ++var9) {
				java.lang.reflect.Type l = var7[var9];
				appendSignature(builder, l, visitEnd);
			}

		} else if (type != null) {
			throw new IllegalArgumentException("Unrecognized type " + type + " " + type.getClass());
		}
	}

	public static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder, boolean appendEnd) {
		Class<?> currentClass = clazz;
		while (currentClass.isArray()) {
			stringBuilder.append('[');
			currentClass = currentClass.getComponentType();
		}
		if (currentClass.isPrimitive()) {
			char descriptor;
			if (currentClass == Integer.TYPE) {
				descriptor = 'I';
			} else if (currentClass == Void.TYPE) {
				descriptor = 'V';
			} else if (currentClass == Boolean.TYPE) {
				descriptor = 'Z';
			} else if (currentClass == Byte.TYPE) {
				descriptor = 'B';
			} else if (currentClass == Character.TYPE) {
				descriptor = 'C';
			} else if (currentClass == Short.TYPE) {
				descriptor = 'S';
			} else if (currentClass == Double.TYPE) {
				descriptor = 'D';
			} else if (currentClass == Float.TYPE) {
				descriptor = 'F';
			} else if (currentClass == Long.TYPE) {
				descriptor = 'J';
			} else {
				throw new AssertionError();
			}
			stringBuilder.append(descriptor);
		} else {
			stringBuilder.append('L').append(getInternalName(currentClass));
			if(appendEnd) stringBuilder.append(';');
		}
	}

	private static String getInternalName(Class<?> type) {
		return type.getName().replace('.', '/');
	}
}
