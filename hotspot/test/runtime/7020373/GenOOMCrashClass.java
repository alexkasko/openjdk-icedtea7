/*
 * Copyright (c) 2011, Red Hat Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

import java.applet.Applet;
import java.io.IOException;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ArrayType;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.JSR;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.bcel.internal.generic.Type;


public class GenOOMCrashClass {

    public static  String genOOMCrashClass(int maxmeth, int nums/*String[] a*/) throws Exception {
        String theClassFile = "OOMCrashClass"+nums+"_"+maxmeth;
        ClassGen cg = new ClassGen(theClassFile, "java.applet.Applet",
                "<generated>", Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
        ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool

        //      int br0 = cp.addClass("marc.schoenefeld.2008");

        int br2 = cp.addMethodref("java.lang.Integer", "parseInt",
                "(Ljava/lang/String;)I");

        Type[] argtype = new Type[] {
            new ArrayType(Type.STRING, 1)
        };

        for (int j = 0; j < maxmeth; j++) {

            InstructionList il = new InstructionList();

            String methodName = maxmeth == 1 ? "main" : "main" + j;
            MethodGen mg = new MethodGen(Constants.ACC_STATIC
                    | Constants.ACC_PUBLIC,// access flags
                    Type.VOID, // return type
                    argtype, new String[] { "argv" }, // arg
                    // names
                    methodName, theClassFile, // method, class
                    il, cp);

            il.append(new ALOAD(0));
            il.append(new ICONST(0));
            il.append(new AALOAD()); // load something unpredictable, no folding
                                     // please

            il.append(new INVOKESTATIC(br2));
            il.append(new ISTORE(1));

            GOTO gototail = new GOTO(null);

            il.append(gototail);

            InstructionHandle ret = il.append(new RETURN());
            InstructionHandle ih = null;
            for (int i = 0; i < nums; i++) {
                ih = il.append(new ILOAD(1));
                IFEQ ifeq = new IFEQ(null);
                il.append(ifeq);

                JSR jsr = new JSR(null);
                GOTO next = new GOTO(null);

                InstructionHandle h_jsr = il.append(jsr);
                InstructionHandle h_goto = il.append(next);
                InstructionHandle h_ret = il.append(new RETURN());

                InstructionHandle danach = il.append(new ACONST_NULL());
                jsr.setTarget(h_ret);
                next.setTarget(danach);

                il.append(new GOTO(ih));
                ifeq.setTarget(ret);
                ret = ih;
            }

            gototail.setTarget(ih);

            mg.setMaxStack(Integer.MAX_VALUE); // Needed stack size

            mg.setMaxLocals();
            cg.addMethod(mg.getMethod());
        }
        /* Add public <init> method, i.e. empty constructor */
        cg.addEmptyConstructor(Constants.ACC_PUBLIC);

        /* Get JavaClass object and dump it to file. */
        try {
            System.out.println("dumping:"+theClassFile);
            cg.getJavaClass().dump(theClassFile + ".class");
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
        return theClassFile;
    }

    public static void main(String[] a) throws Exception {
        int maxmeth_default = 250;
        int nums_default = 20;
        int maxmeth;
        int nums;
        try {
            maxmeth = Integer.parseInt(a[0]);
        }
        catch (Exception e) {
            maxmeth = maxmeth_default;
        }
        try {
            nums = Integer.parseInt(a[1]);
        }
        catch (Exception e) {
            nums = nums_default;
        }       
        String classname = genOOMCrashClass(maxmeth,nums);
        System.out.println("Generated");
        // System.out.println(a[0]);
        // System.out.println("Loading");

        // long t = System.currentTimeMillis();
        // Class g2 = Class.forName(classname);
        // long u = System.currentTimeMillis();
        // System.out.println(g2 + ":" + (u - t));
    }

}
