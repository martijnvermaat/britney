/*

Britney - A simple untyped Lambda Calculus interpreter

This file is part of Britney.
See the file "LICENSE" for copyright information and the
terms and conditions for copying, distribution and
modification of Britney.

*/


package nl.vu.cs.mvermaat.britney;


import nl.vu.cs.mvermaat.britney.grammar.analysis.*;
import nl.vu.cs.mvermaat.britney.grammar.node.*;


public class AlphaConversion extends DepthFirstAdapter {


    private String variable;
    private String substitute;


    /*
      On each occurence of a variable:
      - Check if variable equals variable to rename
      - If it does, rename the variable
    */

    public void caseAVariableTerm(AVariableTerm node) {

        if (node.getVariable() != null) {
            if (variable.equals(node.getVariable().getText())) {
                node.getVariable().setText(substitute);
            }
        }

    }


    /*
      On each occurence of an abstraction:
      - Check if bound variable equals variable to rename
      - If it does, stop here
    */

    public void caseAAbstractionTerm(AAbstractionTerm node) {

        if(node.getVariable() != null
           && !variable.equals(node.getVariable().getText())) {

            if(node.getTerm() != null) {
                node.getTerm().apply(this);
            }

        }

    }


    public void caseAApplicationTerm(AApplicationTerm node) {

        if(node.getFunction() != null) {
            node.getFunction().apply(this);
        }

        if(node.getArgument() != null) {
            node.getArgument().apply(this);
        }

    }


    /*
      Rename all free occurences of variable to substitute
    */

    public AlphaConversion(String variable, String substitute) {

        this.variable = variable;
        this.substitute = substitute;

    }


}
