package test;

import net.devtech.signutil.v0.api.TypeArgument;
import net.devtech.signutil.v0.api.type.reference.ClassType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeArgumentTest {
	@Test
	public void testExtends() {
		TypeArgument argument = TypeArgument.create("+Ljava/lang/Object;");
		Assertions.assertEquals(argument.getWildcard(), '+');
		Assertions.assertEquals(argument.getType(), ClassType.OBJECT);
	}
}
