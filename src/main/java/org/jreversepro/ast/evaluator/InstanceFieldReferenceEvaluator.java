/**
 *  @(#) NonStaticFieldAccessorEvaluator.java
 *
 * JReversePro - Java Decompiler / Disassembler.
 * Copyright (C) 2008 Karthik Kumar.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0 
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 

 **/
package org.jreversepro.ast.evaluator;

import java.util.Arrays;

import org.jreversepro.ast.expression.Assignment;
import org.jreversepro.ast.expression.Expression;
import org.jreversepro.ast.expression.FieldAccessExpression;
import org.jreversepro.ast.expression.InstanceFieldAccessExpression;
import org.jreversepro.ast.intermediate.CompleteLine;
import org.jreversepro.reflect.instruction.Instruction;


/**
 * @author akkumar
 * 
 */
public class InstanceFieldReferenceEvaluator extends
    AbstractInstructionEvaluator {

  /**
   * @param context
   */
  public InstanceFieldReferenceEvaluator(EvaluatorContext context) {
    super(context);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.sf.jrevpro.decompile.evaluator.AbstractInstructionEvaluator#evaluate
   * (net.sf.jrevpro.reflect.instruction.Instruction)
   */
  @Override
  void evaluate(Instruction ins) {
    switch (ins.opcode) {
    case OPCODE_GETFIELD: {
      Expression op1 = evalMachine.pop();

      int offset = ins.getArgUnsignedShort();

      int fieldPtr = pool.getPtr2(offset);

      String fieldName = pool.getFieldName(fieldPtr);
      String fieldType = pool.getFieldType(fieldPtr);

      FieldAccessExpression expr = new InstanceFieldAccessExpression(op1, fieldName,
          fieldType);
      evalMachine.push(expr);
      break;
    }
    case OPCODE_PUTFIELD: {

      Expression rhs = evalMachine.pop();
      Expression accessTarget = evalMachine.pop();

      int offset = ins.getArgUnsignedShort();

      int fieldPtr = pool.getPtr2(offset);
      String fieldName = pool.getFieldName(fieldPtr);
      String fieldType = pool.getFieldType(fieldPtr);

      FieldAccessExpression expr = new InstanceFieldAccessExpression(accessTarget,
          fieldName, fieldType);
      Assignment assign = new Assignment(expr, rhs);
      statements.append(new CompleteLine(ins, assign));
      break;
    }
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.sf.jrevpro.decompile.evaluator.AbstractInstructionEvaluator#
   * getProcessingOpcodes() 
   */
  @Override
  Iterable<Integer> getProcessingOpcodes() {
    return Arrays.asList(OPCODE_GETFIELD, OPCODE_PUTFIELD);
  }

}
