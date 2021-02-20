package test;

import java.util.Arrays;
import java.util.Collections;

import net.devtech.signutil.bounded.MethodSignature;
import net.devtech.signutil.bounded.TypeParameter;
import net.devtech.signutil.type.BaseType;
import net.devtech.signutil.type.VoidType;
import net.devtech.signutil.type.reference.TypeVariable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodSignatureTest {
	@Test
	public void testVoid() {
		MethodSignature signature = MethodSignature.create("()V");
		Assertions.assertTrue(signature.getTypeParameters().isEmpty());
		Assertions.assertTrue(signature.getParameters().isEmpty());
		Assertions.assertEquals(signature.getReturnType(), VoidType.INSTANCE);
	}

	@Test
	public void testParameter() {
		MethodSignature signature = MethodSignature.create("(I)V");
		Assertions.assertIterableEquals(signature.getParameters(), Collections.singletonList(BaseType.INT));
		Assertions.assertEquals(signature.getReturnType(), VoidType.INSTANCE);
	}

	@Test
	public void testGeneric() {
		MethodSignature signature = MethodSignature.create("<T:Ljava/lang/Object;V::Ljava/lang/Runnable;>(TT;)TV;");
		Assertions.assertIterableEquals(signature.getTypeParameters(), Arrays.asList(TypeParameter.create("T:Ljava/lang/Object;"), TypeParameter.create("V::Ljava/lang/Runnable;")));
		Assertions.assertIterableEquals(signature.getParameters(), Collections.singletonList(TypeVariable.createWithIdentifier("T")));
		Assertions.assertEquals(signature.getReturnType(), TypeVariable.createWithIdentifier("V"));
	}

	// todo test changing
}
