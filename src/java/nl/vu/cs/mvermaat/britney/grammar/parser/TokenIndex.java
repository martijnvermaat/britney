/* This file was generated by SableCC (http://www.sablecc.org/). */

package nl.vu.cs.mvermaat.britney.grammar.parser;

import nl.vu.cs.mvermaat.britney.grammar.node.*;
import nl.vu.cs.mvermaat.britney.grammar.analysis.*;

class TokenIndex extends AnalysisAdapter
{
    int index;

    public void caseTHead(THead node)
    {
        index = 0;
    }

    public void caseTBody(TBody node)
    {
        index = 1;
    }

    public void caseTVariable(TVariable node)
    {
        index = 2;
    }

    public void caseTLPar(TLPar node)
    {
        index = 3;
    }

    public void caseTRPar(TRPar node)
    {
        index = 4;
    }

    public void caseEOF(EOF node)
    {
        index = 5;
    }
}
