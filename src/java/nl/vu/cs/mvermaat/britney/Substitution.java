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


public class Substitution extends DepthFirstAdapter {


    private PTerm substitute;
    private String variable;
    private Vector free;
    private NameGenerator gen;


    /*
      On each occurence of a variable:
      - Check if the variable is one to be replaced
      - If it is, replace it by a copy of substitute
    */

    public void caseAVariableTerm(AVariableTerm node) {

        if (node.getVariable() != null) {

            if (variable.equals(node.getVariable().getText())) {
                node.replaceBy((PTerm) substitute.clone());
            }

        }

    }


    /*
      On each occurence of an abstraction:
      - Don't ascend further if bounded variable is the
        variable to be replaced (because it shouldn't be)
      - Check if the bound variable of this abstraction
        is free in the substitution (so it may clash)
      - Rename the bound variable if it is
    */

    public void caseAAbstractionTerm(AAbstractionTerm node) {

        if(!(node.getVariable() != null
            && variable.equals(node.getVariable().getText()))) {

            if(node.getVariable() != null) {

                // Check if bound variable is free in substitute

                if (free.contains(node.getVariable().getText())) {

                    if (node.getTerm() != null) {

                        // Alpha conversion on node.getVariable().getText()

                        // Get list of forbidden names
                        VariableListing v = new VariableListing();
                        node.getTerm().apply(v);

                        String name;

                        do {
                            name = gen.next();
                        } while (free.contains(name) || v.getVariables().contains(name));

                        // Rename free occurences in subtree
                        node.getTerm().apply(new AlphaConversion(node.getVariable().getText(), name));

                        // Rename bounding variable
                        node.getVariable().setText(name);

                    }

                }

            }

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
      Replace all occurences of variable in this tree with
      a copy of substitute.
      Bound variables in the tree that may clash with free
      variables in substitute are renamed.
    */

    public Substitution(PTerm substitute, String variable, NameGenerator gen) {

        this.substitute = substitute;
        this.variable = variable;
        this.gen = gen;

        FreeVariableListing f = new FreeVariableListing();
        this.substitute.apply(f);
        free = f.getVariables();

    }


}
