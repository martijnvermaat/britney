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
import java.util.*;


public class VariableListing extends DepthFirstAdapter {


    private Vector variables;


    /*
      On each occurance of a variable:
      - Add it to variables
        (if it is not already there)
    */

    public void caseAVariableTerm(AVariableTerm node) {

        if (node.getVariable() != null) {

            if (!variables.contains(node.getVariable().getText())) {
                variables.add(node.getVariable().getText());
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


    public void caseAAbstractionTerm(AAbstractionTerm node) {

        if(node.getTerm() != null) {
            node.getTerm().apply(this);
        }

    }


    public Vector getVariables() {

        return variables;

    }


    /*
      All variables that are used in this tree
      are added to the Vector variables.
    */

    public VariableListing() {

        variables = new Vector();

    }


}
