package test;

import java.util.Arrays;

import net.devtech.signutil.v0.api.TypeArgument;
import net.devtech.signutil.v0.api.type.BaseType;
import net.devtech.signutil.v0.api.type.JavaType;
import net.devtech.signutil.v0.api.type.reference.ArrayType;
import net.devtech.signutil.v0.api.type.reference.ClassType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaTypeTest {
	@Test
	public void testClass() {
		Assertions.assertEquals(JavaType.read("Ljava/lang/Object;", 0), ClassType.create("Ljava/lang/Object;"));
	}

	@Test
	public void testObjArray() {
		Assertions.assertEquals(JavaType.read("[Ljava/lang/Object;", 0), ArrayType.create("[Ljava/lang/Object;"));
	}

	@Test
	public void testArray() {
		Assertions.assertEquals(JavaType.read("[Z", 0), ArrayType.create("[Z"));
	}

	@Test
	public void testBase() {
		Assertions.assertEquals(JavaType.read("Z", 0), BaseType.BOOLEAN);
	}

	@Test
	public void testGenerics() {
		ClassType type = ClassType.create("Ltest/Test<LBaz;LFoo;>;");
		Assertions.assertEquals(type.getName(), "test/Test");
		Assertions.assertEquals(type.getInternalName(), "test/Test");
		Assertions.assertIterableEquals(type.getArguments(), Arrays.asList(TypeArgument.create("LBaz;"), TypeArgument.create("LFoo;")));
	}

	@Test
	public void testInner() {
		ClassType type = ClassType.create("Ltest/Test<LBaz;LFoo;>.Inner<[Z+Ltest/Test;>;");
		Assertions.assertIterableEquals(type.getArguments(), Arrays.asList(TypeArgument.create("[Z"), TypeArgument.create("+Ltest/Test;")));
		Assertions.assertEquals(type.getName(), "Inner");
		Assertions.assertEquals(type.getInternalName(), "test/Test$Inner");
		ClassType parent = type.getParent();
		Assertions.assertNotNull(parent);
		Assertions.assertEquals(parent.getName(), "test/Test");
		Assertions.assertEquals(parent.getInternalName(), "test/Test");
		Assertions.assertIterableEquals(parent.getArguments(), Arrays.asList(TypeArgument.create("LBaz;"), TypeArgument.create("LFoo;")));
	}

	@Test
	public void replaceName() {
		ClassType type = ClassType.create("Ljava/lang/Object;");
		Assertions.assertEquals(type.withName("java/lang/Runnable"), ClassType.create("Ljava/lang/Runnable;"));
	}

	@Test
	public void replaceParent() {
		ClassType type = ClassType.create("LTest<[Z>.Object;");
		Assertions.assertEquals(type.withParent(ClassType.create("LBar<[I>;")), ClassType.create("LBar<[I>.Object;"));
	}
}
