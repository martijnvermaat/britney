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


public class BetaReduction extends DepthFirstAdapter {


    private boolean reduced = false;
    private NameGenerator gen;


    /*
      On each occurence of an application:
      - Check if left side is an abstraction
      - If it is, substitute right side for each occurence
        of a variable bound by this abstraction,
        and replace entire application by resulting term
    */

    public void caseAApplicationTerm(AApplicationTerm node) {

        if (node.getFunction() instanceof AAbstractionTerm) {

            // We can reduce here

            AAbstractionTerm abstraction = (AAbstractionTerm) node.getFunction();

            if (abstraction.getTerm() != null
                && abstraction.getVariable() != null
                && node.getArgument() != null) {

                abstraction.getTerm().apply(
                    new Substitution(node.getArgument(), abstraction.getVariable().getText(), gen)
                );

                node.replaceBy(abstraction.getTerm());

                reduced = true;

            }

        } else {

            // Look further

            if(node.getFunction() != null) {
                node.getFunction().apply(this);
            }

            if(node.getArgument() != null && reduced == false) {
                node.getArgument().apply(this);
            }

        }

    }


    public void caseAAbstractionTerm(AAbstractionTerm node) {

        if(node.getTerm() != null) {
            node.getTerm().apply(this);
        }

    }


    public void caseAVariableTerm(AVariableTerm node) {
    }


    public boolean hasReduced() {

        return reduced;

    }


    public BetaReduction(NameGenerator gen) {

        this.gen = gen;

    }


}
