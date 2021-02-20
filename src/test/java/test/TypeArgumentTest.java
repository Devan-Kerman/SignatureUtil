package test;

import net.devtech.signutil.TypeArgument;
import net.devtech.signutil.type.reference.ClassType;
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
