package test;

import java.util.Collections;

import net.devtech.signutil.bounded.TypeParameter;
import net.devtech.signutil.type.reference.ClassType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeParameterTest {
	@Test
	public void testOmittion() {
		TypeParameter parameter = TypeParameter.create("T::Ljava/lang/Runnable;");
		Assertions.assertEquals(parameter.getIdentifier(), "T");
		Assertions.assertEquals(parameter.getClassBound(), ClassType.OBJECT);
		Assertions.assertIterableEquals(parameter.getInterfaceBounds(), Collections.singletonList(ClassType.create("Ljava/lang/Runnable;")));
	}

	@Test
	public void testSuper() {
		TypeParameter parameter = TypeParameter.create("T:Ljava/lang/Integer;:Ljava/lang/Runnable;");
		Assertions.assertEquals(parameter.getIdentifier(), "T");
		Assertions.assertEquals(parameter.getClassBound(), ClassType.INTEGER);
		Assertions.assertIterableEquals(parameter.getInterfaceBounds(), Collections.singletonList(ClassType.create("Ljava/lang/Runnable;")));
	}

	// todo test changing
}
