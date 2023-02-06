package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import org.objectweb.asm.MethodVisitor;
import org.pitest.bytecode.ASMVersion;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

import static org.objectweb.asm.Opcodes.GETSTATIC;

public enum HttpStatusMutator implements MethodMutatorFactory {

    HTTP_STATUS_MUTATOR;

    @Override
    public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
        return new HttpStatusVisitor(this, context, methodVisitor);
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return name();
    }
}

class HttpStatusVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    HttpStatusVisitor(final MethodMutatorFactory factory, final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(ASMVersion.ASM_VERSION, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        if (opcode == GETSTATIC
                && owner.equals("org/springframework/http/HttpStatus")
                && name.equals("NOT_FOUND")
                && descriptor.equals("Lorg/springframework/http/HttpStatus;")) {
            this.context.registerMutation(factory, "Change NOT_FOUND to INTERNAL_SERVER_ERROR");
            this.mv.visitFieldInsn(opcode, owner, "INTERNAL_SERVER_ERROR", descriptor);
        } else {
            this.mv.visitFieldInsn(opcode, owner, name, descriptor);
        }
    }
}