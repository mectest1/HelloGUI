package com.mec.app.plugin.vm;

/**
 * <h3>Note</h3>
 * <p>
 * This enum is used here for another organization of opcodes (a.k.a for documentation use only;
 * You should use {@link OpCode} in your code.
 * </p>
 * <p>ref: https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-7.html</p>
 * <p>ref2: Bill Venners - Inside the JVM</p>
 * @author MEC
 *
 */
//@Deprecated
public enum OpCode2 {

	//=================================================
	//Opcodes on operand stack
	//=================================================

	//-------------------------------------------------
	//Pushing single-word constants onto the stack
	//-------------------------------------------------
	
	iconst_m1
	,iconst_0
	,iconst_1
	,iconst_2
	,iconst_3
	,iconst_4
	,iconst_5
	
	//-------------------------------------------------
	//Pushing dual-word  constants onto the stack
	//-------------------------------------------------
	,lconst_0
	,lconst_1
	
	,fconst_0
	,fconst_1
	,fconst_2
	
	,dconst_0
	,dconst_1
	
	//-------------------------------------------------
	//Pushing a null reference onto the stack
	//-------------------------------------------------
	,aconst_null
	
	
	//-------------------------------------------------
	//Pushing byte and short constants onto the stack 
	//-------------------------------------------------
					//Note: after pushing byte and short onto stack, they will be converted
					//into integer type automatically
	,bipush			//operand: byte1
	,sipush			//operand: byte1, byte2
	
	
	
	
	//-------------------------------------------------
	//Pushing constant pool entries onto the stack
	//-------------------------------------------------
					//ldc and ldc_w push a single-word item onto the operand stack,
					//while ldc2_w push a double-words item onto the operand stack
					//Note: ldc can only refer to constant pool locations one through 255
	,ldc			//operand: indexbyte1
					//Note:l ldc_w can refer to any constant pool location
	,ldc_w			//operands: indexbyte1, indexbyte2
	
					//Note: ldc_w can refer to any constant pool location containing a long or double,
					//which occupies two words.
	,ldc2_w			//operands: indexbyte1, indexbyte2
	
	//-------------------------------------------------
	//Stack manipulation
	//-------------------------------------------------
	,nop			//do nothing (WTF?)
	,pop
	,pop2
	,swap
	,dup			//same as dup_x0 -- if there is one;
	,dup_x1
	,dup_x2
	,dup2			//same as dup2_x0 -- if there ever is such an opcode;
	,dup2_x1
	,dup2_x2
	
	
	
	
	//-------------------------------------------------
	//Operand Stack and Local Variables, Part1: 
	//Pushing local variables onto the stack
	//-------------------------------------------------
	
	
	//-------------------------------------------------
	//Pushing single-word local variables onto the stack
	//-------------------------------------------------
	,iload			//operand: vindex, pushes int from local variable position vindex
	,iload_0
	,iload_1
	,iload_2
	,iload_3
	
	,fload			//operand: vindex
	,fload_0
	,fload_1
	,fload_2
	,fload_3
	
	
	//-------------------------------------------------
	//Pushing dual-word local variables onto the stack
	//-------------------------------------------------
	,lload			//operand: vindex, pushes long from local variable positions vindex and (vindex+1)
	,lload_0		//pushes long from local variable positions zero and one
	,lload_1
	,lload_2
	,lload_3
	
	,dload			//operand: vindex, pushes double from local variable positions vindex and (vindex+1)
	,dload_0
	,dload_1
	,dload_2
	,dload_3
	
	//-------------------------------------------------
	//Pushing object reference local variables onto the stack
	//-------------------------------------------------
	,aload			//operand: vindex, pushes object reference from local variable position vindex
	,aload_0
	,aload_1
	,aload_2
	,aload_3
	
	
	//-------------------------------------------------
	//Operand Stack and Local Variables, Part2: 
	//Pops top of the stack back into local variables;
	//-------------------------------------------------
	
	
	//-------------------------------------------------
	//Popping single-word values into local variables
	//-------------------------------------------------
	,istore			//operand: vindex, pops int to local variable position vindex
	,istore_0
	,istore_1
	,istore_2
	,istore_3
	
	,fstore			//operand: vindex, pops float to local variable position vindex
	,fstore_0
	,fstore_1
	,fstore_2
	,fstore_3
	
	
	//-------------------------------------------------
	//Popping dual-word values into local variables
	//-------------------------------------------------
	,lstore			//operand: vindex, pops long to local variables positions vindex and (vindex+1)
	,lstore_0		//pops long to local variables positions zero and one
	,lstore_1
	,lstore_2
	,lstore_3
	
	,dstore			//operand: vindex, pops double to local variables positions vindex and (vindex+1)
	,dstore_0		//pops double to local variable positions zero and one
	,dstore_1
	,dstore_2
	,dstore_3
	
	//-------------------------------------------------
	//Popping object references into local variables
	//-------------------------------------------------
	,astore			//operand: vindex, pops object reference to local variable position vindex
	,astore_0		//pops object reference to local variable position zero
	,astore_1
	,astore_2
	,astore_3
	
	
	//-------------------------------------------------
	//The wide instruction
	//-------------------------------------------------
					//Note: the "wide" opcode modifies other opcodes.
					//"wide" can precede an instruction, such as iload,
					//that taks an 8-bit unsigned local variable index.
					//Two bytes that form a 16-bit unsigned index into the local 
					//variables follows the wide opcode and the modified opcode.
	,wide			//Available instructions to be widened:
					//(First form:)
					//iload, fload, aload, lload, dload, 
					//istore, fstore, astore, lstore, dstore, 
					//ret
					//(Second form:)
					//iinc
					//ref: https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.wide
	
	
	//=================================================
	//Type Conversion
	//=================================================
	,i2l
	,i2f
	,i2d
	,l2i
	,l2f
	,l2d
	,f2i
	,f2l
	,f2d
	,d2i
	,d2l
	,d2f
	
	,i2b
	,i2c
	,i2s
	//=================================================
	//Integer Arithmetic
	//=================================================
	//-------------------------------------------------
	//Integer addition
	//-------------------------------------------------
	,iadd
	,ladd
	,iinc			//operands: vindex, const;
					//or use: wide iinc, indexbyte1, indexbyte2, constbyte1, constbyte2
	//-------------------------------------------------
	//Integer subtraction
	//-------------------------------------------------
	,isub
	,lsub
	//-------------------------------------------------
	//Integer multiplication
	//-------------------------------------------------
	,imul
	,lmul
	//-------------------------------------------------
	//Integer devision
	//-------------------------------------------------
	,idiv
	,ldiv
	//-------------------------------------------------
	//Integer remainder
	//-------------------------------------------------
	,irem
	,lrem
	//-------------------------------------------------
	//Integer negation
	//-------------------------------------------------
	,ineg
	,lneg
	
	//=================================================
	//Logic
	//=================================================
	//-------------------------------------------------
	//Shifting ints
	//-------------------------------------------------
	,ishl
	,ishr
	,iushr
	,lshl
	,lshr
	,lushr
	
	
	//-------------------------------------------------
	//Bitwise logical operations
	//-------------------------------------------------
	,iand
	,ior
	,ixor
	,land
	,lor
	,lxor
	
	
	//=================================================
	//Floating Point Arithmetic
	//=================================================
	//-------------------------------------------------
	//Float-points number arithmetics
	//-------------------------------------------------
	,fadd
	,dadd
	
	,fsub
	,dsub
	
	,fmul
	,dmul
	
	,fdiv
	,ddiv
	
	,frem			//use Math.IEEEremainder() to get the IEEE compliant remainder of floats 
	,drem
	
	,fneg
	,dneg
	
	
	//=================================================
	//Object and Arrays: instructions that deal with objects
	//=================================================
	//-------------------------------------------------
	//Object operations
	//-------------------------------------------------
					//Actually, it's "new", but since "new" is a keyword in Java Language, so~
	,New			//operands: indexbyte1, indexbyte2
	,putfield		//operands: indexbyte1, indexbyte2
	,getfield		//operands: indexbyte1, indexbyte2
	
	,putstatic		//operands: indexbyte1, indexbyte2
	,getstatic		//operands: indexbyte1, indexbyte2
	
	
	//-------------------------------------------------
	//Type checking
	//-------------------------------------------------
	,checkcast		//operands: indexbyte1, indexbyte2
	,instanceOf		//operands: indexbyte1, indexbyte2
	
	//-------------------------------------------------
	//Array operations Part 1: create, get length
	//-------------------------------------------------
	,newarray		//operand: atype
	,anewarray		//operands: indexbyte1, indexbyte2
	,multianewarray	//operands: indexbyte1, indexbyte2, dimensions
	
	,arraylength
	
	
	//-------------------------------------------------
	//Array operations Part2: put and get elements
	//-------------------------------------------------
	,baload			//Note: for both bytes and booleans
	,caload
	,saload
	,iaload
	,laload
	,faload
	,daload
	,aaload
	
	,bastore		//the lower eight bits of the popped int value will be stored
	,castore
	,sastore		//As with castore, the lower sixteen bits of the popped int value will be stored
	,iastore
	,lastore
	,fastore
	,dastore
	,aastore
	
	
	//=================================================
	//Control Flow
	//=================================================
	//-------------------------------------------------
	//Conditional branch Part 1: integer comparison with zero
	//-------------------------------------------------
	,ifeq			//operands: branchbyte1, branchbyte2
	,ifne			//operands: branchbyte1, branchbyte2
	,iflt			//operands: branchbyte1, branchbyte2
	,ifle			//operands: branchbyte1, branchbyte2
	,ifgt			//operands: branchbyte1, branchbyte2
	,ifge			//operands: branchbyte1, branchbyte2
	
	//-------------------------------------------------
	//Conditional branch Part 2: comparison of two integers
	//-------------------------------------------------
	,if_icmpeq		//operands: branchbyte1, branchbyte2
	,if_icmpne
	,if_icmplt
	,if_icmple
	,if_icmpgt
	,if_icmpge
	
	//-------------------------------------------------
	//Conditional branch Part 3: comparison of longs, floats, and doubles
	//-------------------------------------------------
	//Note: These opcodes don't cause a branch by themselves.
	//Instead, they push the int value that represents the result of 
	//the comparison: 0 for equal to, 1 to reater than, and -1 for less than,
	//, and then se one of the int compare opcodes introduced above to 
	//force the actual branch
	,lcmp			//operand: none
	,fcmpg			//operand: none. pushes 1 for NaN
	,fcmpl			//operand: none. pushes -1 for NaN
	,dcmpg			//operand: none. pushes 1 for NaN
	,dcmpl			//operand: none. pushes -1 for NaN
	
	//-------------------------------------------------
	//Conditional branch Part 4: object reference comparison with null
	//-------------------------------------------------
	,ifnull			//operands: branchbyte1, branchbyte2
	,ifnonnull
	
	//-------------------------------------------------
	//Conditional branch Part 5: comparison of two object references
	//-------------------------------------------------
	,if_acmpeq		//operands: branchbyte1, branchbyte2
	,if_acmpne
	
	//-------------------------------------------------
	//Unconditional branch
	//-------------------------------------------------
	,Goto			//operands: branchbyte1, branchbyte2
	,goto_w			//operands: branchbyte1, branchbyte2, branchbyte3, branchbyte4
	
	
	//-------------------------------------------------
	//Conditional branching with tables (table jumping instructions)
	//-------------------------------------------------
	,lookupswitch	//operands: 0~3 padding byte, 
					//defaultbyte1, defaultbyte2, defaultbyte2, defaultbyte4
					//npairs1, npairs2, npairs3, npairs4
					//case value/branch offset pairs, ...
	,tableswitch	//operands: 0~3 padding byte,
					//defaultbyte1, defaultbyte2, defaultbyte3, defaultbyte4
					//lowbyte1, lowbyte2, lowbyte3, lowbyte4
					//hightbyte1, highbyte2, highbyte3, highbyte4
					//branch offsets, ...
	
	//=================================================
	//Exceptions
	//=================================================
	//-------------------------------------------------
	//Throwing Exceptions;
	//-------------------------------------------------
	//Note: Exception table for the method;
	//From, To, Target(branch offset), Type(to catch)
	,athrow			//operand: none
	
	
	//=================================================
	//Finally Clauses
	//=================================================
	//-------------------------------------------------
	//Finally clauses: miniature subroutines;
	//-------------------------------------------------
	/**
	 * Asymmetrical Invocations and Return
	 * 
	 * Note that <code>jsr/jsr_w</code> pushes return address onto operand stack before
	 * branching off to the subroutine, yet <code>ret/wide ret</code> retrieves this return
	 * address from local variable slot, why? 
	 * 
	 * The reason is to implement break/continue/return/throw in finally{} clause (a.k.a
	 * miniature subroutine)
	 * 
	 * Thus at the start of each subroutine, the return address is popped off
	 * the top of the stack and stored in a local variable--the same variable 
	 * from which the <code>ret</code> instruction later gets it.
	 * (Since the <code>ret</code> may never get executed)
	 */
					//Note: jsr: Jumps to Sub-routine
					//pushes the return address (following this jsr opcode) 
					//and branches to offset
	,jsr			//operands: branchbyte1, branchbyte2
	,jsr_w			//operands: branchbyte1, branchbyte2, branchbyte3, branchbyte4
	
					//Note: returns to the address stored in local variable index
	,ret			//operand: index
					//Another form: wide ret indexbyte1, indexbyte2
	
	//=================================================
	//Method Invocations and Return
	//=================================================
	//-------------------------------------------------
	//Method invocation
	//-------------------------------------------------
					//Invoke instance method
	,invokevirtual	//operands: indexbyte1, indexbyte2
					//Invoke class method
	,invokestatic	//operands: indexbyte1, indexbyte2
	
						//Note: invoke special normally selects a method based on the 
						//type of the reference rather than the class of the object
						//For invocation of <init>, private instance methods, and super methods;
	,invokeinterface	//operands: indexbyte1, indexbyte2, 
	,invokespecial		//operands: indexbyte1, indexbyte2
	
	
					//Funny fact: up until JDK7, opcode 0xba(186) is not used.
	,invokedynamic	//operands: indexbyte1, indexbyte2, 0, 0
	//-------------------------------------------------
	//Returning from methods
	//-------------------------------------------------
	,ireturn		//operand: none
	,lreturn
	,freturn
	,dreturn
					//operand: none. pop object reference, 
	,areturn		//push onto stack of calling method and return
	,Return			//operand: none, Return void
	
	//=================================================
	//Thread Synchronizations
	//=================================================
	//-------------------------------------------------
	//Monitors
	//-------------------------------------------------
					//pops objectRef, acquire the lock associated with this object
	,monitorenter	//operand: none
					//pops objectRee, release the lock associated with this object
	,monitorexit	//operand: none
	
	
	
	
	//=================================================
	//Used by JVM
	//=================================================
	//-------------------------------------------------
	//Reserved opcodes
	//-------------------------------------------------
	,breakpoint
	,impdep1
	,impdep2
	//-------------------------------------------------
	//
	//-------------------------------------------------

//
//	
//	//=================================================
//	//Quick Opcode
//	//=================================================
//	,ldc_quick(0xcb)
//	,ldc_w_quick(0xcc)
//	,ldc2_w_quick(0xcd)
//	,getfield_quick(0xce)
//	,putfield_quick(0xcf)
//	,getfield2_quick(0xd0)
//	,putfield2_quick(0xd1)
//	,getstatic_quick(0xd2)
//	,putstatic_quick(0xd3)
//	,getstatic2_quick(0xd4)
//	,putstatic2_quick(0xd5)
//	,invokevirtual_quick(0xd6)
//	,invokenonvirtual_quick(0xd7)
//	,invokesuper_quick(0xd8)
//	,invokestatic_quick(0xd9)
//	,invokeinterface_quick(0xda)
//	,invokevirtualobject_quick(0xdb)
////	,(0xdc)
//	,new_quick(0xdd)
//	,anewarray_quick(0xde)
//	,multianewarray_quick(0xdf)
//	,checkcast_quick(0xe0)
//	,instanceof_quick(0xe1)
//	,invokevirtual_quick_w(0xe2)
//	,getfield_quick_w(0xe3)
//	,putfield_quick_w(0xe4)
//	
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//=================================================
	//
	//=================================================
	
	//-------------------------------------------------
	//
	//-------------------------------------------------
	
	//=================================================
	//
	//=================================================
	;
	
	
	
	
//	private OpCode(int opcode){
//		this.opcode = opcode;
//	}
//	
//	private int opcode;
//	
	
	
	
	
	
	private OpCode2(){
		this.opcode = OpCode.valueOf(this.name());
	}
	
	private OpCode opcode;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
