# SignatureUtil
An actually good signature api for asm signatures

```java
import java.util.ArrayList;

import net.devtech.signutil.v0.api.*;
import net.devtech.signutil.v0.api.bounded.*;
import net.devtech.signutil.v0.api.type.*;
import net.devtech.signutil.v0.api.type.reference.*;

public class Examples {
	public void classSignatures() {
		ClassSignature signature = ClassSignature.create(
				"<T:Ljava/lang/Object;V::Ljava/lang/Runnable;>Ljava/lang/ref/Reference<TV;>;Ljava/util/function/Consumer<TT;>;");

		// read and parse signatures
		List<TypeParameter> parameters = signature.getTypeParameters();
		for (TypeParameter parameter : parameters) {
			// T, V
			System.out.println(parameter.getIdentifier());
			// Ljava/lang/Object;
			System.out.println(parameter.getClassBound());
			// [], [Ljava/lang/Runnable;]
			System.out.println(parameter.getInterfaceBounds());
		}

		// generate and modify signatures
		List<TypeParameter> change = new ArrayList<>(parameters);
		change.set(0, change.get(0).withIdentifier("C")); // T -> C
        
        // <C:Ljava/lang/Object;V::Ljava/lang/Runnable;>Ljava/lang/ref/Reference<TV;>;Ljava/util/function/Consumer<TT;>;
        System.out.println(signature.withTypeParameters());
	}
	
	// there's MethodSignature too!
}
```