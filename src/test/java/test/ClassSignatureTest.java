package test;

import java.util.Arrays;
import java.util.Collections;

import net.devtech.signutil.v0.api.bounded.ClassSignature;
import net.devtech.signutil.v0.api.bounded.TypeParameter;
import net.devtech.signutil.v0.api.type.reference.ClassType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassSignatureTest {
	@Test
	public void testObj() {
		ClassSignature signature = ClassSignature.create("Ljava/lang/Object;");
		Assertions.assertIterableEquals(signature.getTypeParameters(), Collections.emptyList());
		Assertions.assertEquals(signature.getSuperClass(), ClassType.OBJECT);
		Assertions.assertIterableEquals(signature.getInterfaces(), Collections.emptyList());
	}

	@Test
	public void testInterface() {
		ClassSignature signature = ClassSignature.create("Ljava/lang/Object;Ljava/lang/Runnable;");
		Assertions.assertIterableEquals(signature.getTypeParameters(), Collections.emptyList());
		Assertions.assertEquals(signature.getSuperClass(), ClassType.OBJECT);
		Assertions.assertIterableEquals(signature.getInterfaces(), Collections.singletonList(ClassType.create("Ljava/lang/Runnable;")));
	}

	@Test
	public void testGeneric() {
		ClassSignature signature = ClassSignature.create("<T:Ljava/lang/Object;V::Ljava/lang/Runnable;>Ljava/lang/ref/Reference<TV;>;Ljava/util/function/Consumer<TT;>;");
		Assertions.assertIterableEquals(signature.getTypeParameters(), Arrays.asList(TypeParameter.create("T:Ljava/lang/Object;"), TypeParameter.create("V:Ljava/lang/Object;:Ljava/lang/Runnable;")));
		Assertions.assertEquals(signature.getSuperClass(), ClassType.create("Ljava/lang/ref/Reference<TV;>;"));
		Assertions.assertIterableEquals(signature.getInterfaces(), Collections.singletonList(ClassType.create("Ljava/util/function/Consumer<TT;>;")));
	}

	// todo test changing
}
