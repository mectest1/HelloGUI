package com.mec.app.plugin.vm;

/**
 * <p>Refer to the <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-7.html">
 * JVM instruction set list</a> for all opcodes.</p>
 * 
 * @author MEC
 *
 */
public enum OpCode {

	//Constants:
	 nop					(0, 0x00)    
	,aconst_null			(1, 0x01)    
	,iconst_m1				(2, 0x02)    
	,iconst_0				(3, 0x03)    
	,iconst_1				(4, 0x04)    
	,iconst_2				(5, 0x05)    
	,iconst_3				(6, 0x06)    
	,iconst_4				(7, 0x07)    
	,iconst_5				(8, 0x08)    
	,lconst_0				(9, 0x09)    
	,lconst_1				(10, 0x0a)    
	,fconst_0				(11, 0x0b)    
	,fconst_1				(12, 0x0c)    
	,fconst_2				(13, 0x0d)    
	,dconst_0				(14, 0x0e)    
	,dconst_1				(15, 0x0f)    
	,bipush					(16, 0x10)    
	,sipush					(17, 0x11)    
	,ldc					(18, 0x12)    
	,ldc_w					(19, 0x13)    
	,ldc2_w					(20, 0x14)    
	
	 
	 
	 
	 //Loads
	,iload					(21, 0x15)    
	,lload					(22, 0x16)    
	,fload					(23, 0x17)    
	,dload					(24, 0x18)    
	,aload					(25, 0x19)    
	,iload_0				(26, 0x1a)    
	,iload_1				(27, 0x1b)    
	,iload_2				(28, 0x1c)    
	,iload_3				(29, 0x1d)    
	,lload_0				(30, 0x1e)    
	,lload_1				(31, 0x1f)    
	,lload_2				(32, 0x20)    
	,lload_3				(33, 0x21)    
	,fload_0				(34, 0x22)    
	,fload_1				(35, 0x23)    
	,fload_2				(36, 0x24)    
	,fload_3				(37, 0x25)    
	,dload_0				(38, 0x26)    
	,dload_1				(39, 0x27)    
	,dload_2				(40, 0x28)    
	,dload_3				(41, 0x29)    
	,aload_0				(42, 0x2a)    
	,aload_1				(43, 0x2b)    
	,aload_2				(44, 0x2c)    
	,aload_3				(45, 0x2d)    
	,iaload					(46, 0x2e)    
	,laload					(47, 0x2f)    
	,faload					(48, 0x30)    
	,daload					(49, 0x31)    
	,aaload					(50, 0x32)    
	,baload					(51, 0x33)    
	,caload					(52, 0x34)    
	,saload					(53, 0x35)    
	
	 
	 //Store
	,istore					(54, 0x36)    
	,lstore					(55, 0x37)    
	,fstore					(56, 0x38)    
	,dstore					(57, 0x39)    
	,astore					(58, 0x3a)    
	,istore_0				(59, 0x3b)    
	,istore_1				(60, 0x3c)    
	,istore_2				(61, 0x3d)    
	,istore_3				(62, 0x3e)    
	,lstore_0				(63, 0x3f)    
	,lstore_1				(64, 0x40)    
	,lstore_2				(65, 0x41)    
	,lstore_3				(66, 0x42)    
	,fstore_0				(67, 0x43)    
	,fstore_1				(68, 0x44)    
	,fstore_2				(69, 0x45)    
	,fstore_3				(70, 0x46)    
	,dstore_0				(71, 0x47)    
	,dstore_1				(72, 0x48)    
	,dstore_2				(73, 0x49)    
	,dstore_3				(74, 0x4a)    
	,astore_0				(75, 0x4b)    
	,astore_1				(76, 0x4c)    
	,astore_2				(77, 0x4d)    
	,astore_3				(78, 0x4e)    
	,iastore				(79, 0x4f)    
	,lastore				(80, 0x50)    
	,fastore				(81, 0x51)    
	,dastore				(82, 0x52)    
	,aastore				(83, 0x53)    
	,bastore				(84, 0x54)    
	,castore				(85, 0x55)    
	,sastore				(86, 0x56)    
	 
	 
	 //Stack
	,pop					(87, 0x57)    
	,pop2					(88, 0x58)    
	,dup					(89, 0x59)    
	,dup_x1					(90, 0x5a)    
	,dup_x2					(91, 0x5b)    
	,dup2					(92, 0x5c)    
	,dup2_x1				(93, 0x5d)    
	,dup2_x2				(94, 0x5e)    
	,swap					(95, 0x5f)    
	 
	 
	 
	 //Math
	,iadd					(96, 0x60)    
	,ladd					(97, 0x61)    
	,fadd					(98, 0x62)    
	,dadd					(99, 0x63)    
	,isub					(100, 0x64)   
	,lsub					(101, 0x65)   
	,fsub					(102, 0x66)   
	,dsub					(103, 0x67)   
	,imul					(104, 0x68)   
	,lmul					(105, 0x69)   
	,fmul					(106, 0x6a)   
	,dmul					(107, 0x6b)    
	,idiv					(108, 0x6c)    
	,ldiv					(109, 0x6d)    
	,fdiv					(110, 0x6e)    
	,ddiv					(111, 0x6f)    
	,irem					(112, 0x70)    
	,lrem					(113, 0x71)    
	,frem					(114, 0x72)    
	,drem					(115, 0x73)    
	,ineg					(116, 0x74)    
	,lneg					(117, 0x75)    
	,fneg					(118, 0x76)    
	,dneg					(119, 0x77)    
	,ishl					(120, 0x78)    
	,lshl					(121, 0x79)    
	,ishr					(122, 0x7a)    
	,lshr					(123, 0x7b)    
	,iushr					(124, 0x7c)    
	,lushr					(125, 0x7d)    
	,iand					(126, 0x7e)    
	,land					(127, 0x7f)    
	,ior					(128, 0x80)    
	,lor					(129, 0x81)    
	,ixor					(130, 0x82)    
	,lxor					(131, 0x83)    
	,iinc					(132, 0x84)    
	 
	 
	 
	 //Conversion
	,i2l					(133, 0x85)    
	,i2f					(134, 0x86)    
	,i2d					(135, 0x87)    
	,l2i					(136, 0x88)    
	,l2f					(137, 0x89)    
	,l2d					(138, 0x8a)    
	,f2i					(139, 0x8b)    
	,f2l					(140, 0x8c)    
	,f2d					(141, 0x8d)    
	,d2i					(142, 0x8e)    
	,d2l					(143, 0x8f)    
	,d2f					(144, 0x90)    
	,i2b					(145, 0x91)    
	,i2c					(146, 0x92)    
	,i2s					(147, 0x93)    
	 
	 
	 
	 //Comparisons
	,lcmp					(148, 0x94)    
	,fcmpl					(149, 0x95)    
	,fcmpg					(150, 0x96)    
	,dcmpl					(151, 0x97)    
	,dcmpg					(152, 0x98)    
	,ifeq					(153, 0x99)    
	,ifne					(154, 0x9a)    
	,iflt					(155, 0x9b)    
	,ifge					(156, 0x9c)    
	,ifgt					(157, 0x9d)    
	,ifle					(158, 0x9e)    
	,if_icmpeq				(159, 0x9f)    
	,if_icmpne				(160, 0xa0)    
	,if_icmplt				(161, 0xa1)    
	,if_icmpge				(162, 0xa2)    
	,if_icmpgt				(163, 0xa3)    
	,if_icmple				(164, 0xa4)    
	,if_acmpeq				(165, 0xa5)    
	,if_acmpne				(166, 0xa6)    
	 
	 
	 //References:
	,getstatic				(178, 0xb2)    
	,putstatic				(179, 0xb3)    
	,getfield				(180, 0xb4)    
	,putfield				(181, 0xb5)    
	,invokevirtual			(182, 0xb6)    
	,invokespecial			(183, 0xb7)    
	,invokestatic			(184, 0xb8)    
	,invokeinterface		(185, 0xb9)    
	,invokedynamic			(186, 0xba)    
	,New					(187, 0xbb)    
	,newarray				(188, 0xbc)    
	,anewarray				(189, 0xbd)    
	,arraylength			(190, 0xbe)    
	,athrow					(191, 0xbf)    
	,checkcast				(192, 0xc0)    
	,instanceOf				(193, 0xc1)    
	,monitorenter			(194, 0xc2)    
	,monitorexit			(195, 0xc3)    
	 
	 
	 //Control
	,Goto					(167, 0xa7)    
	,jsr					(168, 0xa8)    
	,ret					(169, 0xa9)    
	,tableswitch			(170, 0xaa)    
	,lookupswitch			(171, 0xab)    
	,ireturn				(172, 0xac)    
	,lreturn				(173, 0xad)    
	,freturn				(174, 0xae)    
	,dreturn				(175, 0xaf)    
	,areturn				(176, 0xb0)    
	,Return					(177, 0xb1)    
	 
	 
	 
	 //Extended
	,wide					(196, 0xc4)   
	,multianewarray			(197, 0xc5)   
	,ifnull					(198, 0xc6)   
	,ifnonnull				(199, 0xc7)   
	,goto_w					(200, 0xc8)   
	,jsr_w					(201, 0xc9)   
	 
	 
	 //Reserved
	,breakpoint				(202, 0xca)    
	,impdep1				(254, 0xfe)    
	,impdep2				(255, 0xff)    
	 

//	//Quick Opcodes
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
//	//,(0xdc)
//	,new_quick(0xdd)
//	,anewarray_quick(0xde)
//	,multianewarray_quick(0xdf)
//	,checkcast_quick(0xe0)
//	,instanceof_quick(0xe1)
//	,invokevirtual_quick_w(0xe2)
//	,getfield_quick_w(0xe3)
//	,putfield_quick_w(0xe4)
//	
		
	;
	
	private OpCode(int opcode){
		this.opcode = opcode;
	}
	
	private OpCode(int dec, int hex){
//		this.opcodeInDecimal = (byte)dec;
//		this.opcodeInHex = (byte)hex;
		//opcode = (byte)dec;	//Nah, since type "byte" is signed number
		opcode = dec;
	}
	
	public int getCode(){
		return opcode;
	}
	
	
	private String mnemonic = this.name();
//	private byte opcodeInHex;
//	private byte opcodeInDecimal;
	private int opcode;
}
