/* This file was generated by SableCC (http://www.sablecc.org/). */

package nl.vu.cs.mvermaat.britney.grammar.parser;

import nl.vu.cs.mvermaat.britney.grammar.lexer.*;
import nl.vu.cs.mvermaat.britney.grammar.node.*;
import nl.vu.cs.mvermaat.britney.grammar.analysis.*;
import java.util.*;

import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class Parser
{
    public final Analysis ignoredTokens = new AnalysisAdapter();

    protected ArrayList nodeList;

    private final Lexer lexer;
    private final ListIterator stack = new LinkedList().listIterator();
    private int last_shift;
    private int last_pos;
    private int last_line;
    private Token last_token;
    private final TokenIndex converter = new TokenIndex();
    private final int[] action = new int[2];

    private final static int SHIFT = 0;
    private final static int REDUCE = 1;
    private final static int ACCEPT = 2;
    private final static int ERROR = 3;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;

        if(actionTable == null)
        {
            try
            {
                DataInputStream s = new DataInputStream(
                    new BufferedInputStream(
                    Parser.class.getResourceAsStream("parser.dat")));

                // read actionTable
                int length = s.readInt();
                actionTable = new int[length][][];
                for(int i = 0; i < actionTable.length; i++)
                {
                    length = s.readInt();
                    actionTable[i] = new int[length][3];
                    for(int j = 0; j < actionTable[i].length; j++)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            actionTable[i][j][k] = s.readInt();
                        }
                    }
                }

                // read gotoTable
                length = s.readInt();
                gotoTable = new int[length][][];
                for(int i = 0; i < gotoTable.length; i++)
                {
                    length = s.readInt();
                    gotoTable[i] = new int[length][2];
                    for(int j = 0; j < gotoTable[i].length; j++)
                    {
                        for(int k = 0; k < 2; k++)
                        {
                            gotoTable[i][j][k] = s.readInt();
                        }
                    }
                }

                // read errorMessages
                length = s.readInt();
                errorMessages = new String[length];
                for(int i = 0; i < errorMessages.length; i++)
                {
                    length = s.readInt();
                    StringBuffer buffer = new StringBuffer();

                    for(int j = 0; j < length; j++)
                    {
                        buffer.append(s.readChar());
                    }
                    errorMessages[i] = buffer.toString();
                }

                // read errors
                length = s.readInt();
                errors = new int[length];
                for(int i = 0; i < errors.length; i++)
                {
                    errors[i] = s.readInt();
                }

                s.close();
            }
            catch(Exception e)
            {
                throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
            }
        }
    }

    private int goTo(int index)
    {
        int state = state();
        int low = 1;
        int high = gotoTable[index].length - 1;
        int value = gotoTable[index][0][1];

        while(low <= high)
        {
            int middle = (low + high) / 2;

            if(state < gotoTable[index][middle][0])
            {
                high = middle - 1;
            }
            else if(state > gotoTable[index][middle][0])
            {
                low = middle + 1;
            }
            else
            {
                value = gotoTable[index][middle][1];
                break;
            }
        }

        return value;
    }

    private void push(int numstate, ArrayList listNode) throws ParserException, LexerException, IOException
    {
	this.nodeList = listNode;

        if(!stack.hasNext())
        {
            stack.add(new State(numstate, this.nodeList));
            return;
        }

        State s = (State) stack.next();
        s.state = numstate;
        s.nodes = this.nodeList;
    }

    private int state()
    {
        State s = (State) stack.previous();
        stack.next();
        return s.state;
    }

    private ArrayList pop()
    {
        return (ArrayList) ((State) stack.previous()).nodes;
    }

    private int index(Switchable token)
    {
        converter.index = -1;
        token.apply(converter);
        return converter.index;
    }

    public Start parse() throws ParserException, LexerException, IOException
    {
        push(0, null);

        List ign = null;
        while(true)
        {
            while(index(lexer.peek()) == -1)
            {
                if(ign == null)
                {
                    ign = new TypedLinkedList(NodeCast.instance);
                }

                ign.add(lexer.next());
            }

            if(ign != null)
            {
                ignoredTokens.setIn(lexer.peek(), ign);
                ign = null;
            }

            last_pos = lexer.peek().getPos();
            last_line = lexer.peek().getLine();
            last_token = lexer.peek();

            int index = index(lexer.peek());
            action[0] = actionTable[state()][0][1];
            action[1] = actionTable[state()][0][2];

            int low = 1;
            int high = actionTable[state()].length - 1;

            while(low <= high)
            {
                int middle = (low + high) / 2;

                if(index < actionTable[state()][middle][0])
                {
                    high = middle - 1;
                }
                else if(index > actionTable[state()][middle][0])
                {
                    low = middle + 1;
                }
                else
                {
                    action[0] = actionTable[state()][middle][1];
                    action[1] = actionTable[state()][middle][2];
                    break;
                }
            }

            switch(action[0])
            {
                case SHIFT:
		    {
		        ArrayList list = new ArrayList();
		        list.add(lexer.next());
                        push(action[1], list);
                        last_shift = action[1];
                    }
		    break;
                case REDUCE:
                    switch(action[1])
                    {

                    case 0:
		    {
			ArrayList list = new0();
			push(goTo(0), list);
		    }
		    break;


                    case 1:
		    {
			ArrayList list = new1();
			push(goTo(0), list);
		    }
		    break;


                    case 2:
		    {
			ArrayList list = new2();
			push(goTo(1), list);
		    }
		    break;


                    case 3:
		    {
			ArrayList list = new3();
			push(goTo(1), list);
		    }
		    break;


                    case 4:
		    {
			ArrayList list = new4();
			push(goTo(2), list);
		    }
		    break;


                    case 5:
		    {
			ArrayList list = new5();
			push(goTo(2), list);
		    }
		    break;


                    case 6:
		    {
			ArrayList list = new6();
			push(goTo(3), list);
		    }
		    break;


                    case 7:
		    {
			ArrayList list = new7();
			push(goTo(3), list);
		    }
		    break;

                    }
                    break;
                case ACCEPT:
                    {
                        EOF node2 = (EOF) lexer.next();
                        PTerm node1 = (PTerm) ((ArrayList)pop()).get(0);
                        Start node = new Start(node1, node2);
                        return node;
                    }
                case ERROR:
                    throw new ParserException(last_token,
                        "[" + last_line + "," + last_pos + "] " +
                        errorMessages[errors[action[1]]]);
            }
        }
    }



    ArrayList new0()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList2 = (ArrayList) pop();
        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        ptermNode1 = (PTerm)nodeArrayList2.get(0);
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new1()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        ptermNode1 = (PTerm)nodeArrayList1.get(0);
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new2()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList2 = (ArrayList) pop();
        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        {
        PTerm ptermNode2;
        PTerm ptermNode3;
        ptermNode2 = (PTerm)nodeArrayList1.get(0);
        ptermNode3 = (PTerm)nodeArrayList2.get(0);

        ptermNode1 = new AApplicationTerm(ptermNode2, ptermNode3);
        }
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new3()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        ptermNode1 = (PTerm)nodeArrayList1.get(0);
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new4()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        {
        TVariable tvariableNode2;
        tvariableNode2 = (TVariable)nodeArrayList1.get(0);

        ptermNode1 = new AVariableTerm(tvariableNode2);
        }
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new5()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList3 = (ArrayList) pop();
        ArrayList nodeArrayList2 = (ArrayList) pop();
        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        ptermNode1 = (PTerm)nodeArrayList2.get(0);
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new6()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList2 = (ArrayList) pop();
        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        {
        TVariable tvariableNode2;
        PTerm ptermNode3;
        tvariableNode2 = (TVariable)nodeArrayList1.get(0);
        ptermNode3 = (PTerm)nodeArrayList2.get(0);

        ptermNode1 = new AAbstractionTerm(tvariableNode2, ptermNode3);
        }
	nodeList.add(ptermNode1);
        return nodeList;
    }



    ArrayList new7()
    {
        ArrayList nodeList = new ArrayList();

        ArrayList nodeArrayList2 = (ArrayList) pop();
        ArrayList nodeArrayList1 = (ArrayList) pop();
        PTerm ptermNode1;
        ptermNode1 = (PTerm)nodeArrayList2.get(0);
	nodeList.add(ptermNode1);
        return nodeList;
    }



    private static int[][][] actionTable;
/*      {
			{{-1, ERROR, 0}, {0, SHIFT, 1}, {2, SHIFT, 2}, {3, SHIFT, 3}, },
			{{-1, ERROR, 1}, {1, SHIFT, 7}, {2, SHIFT, 8}, },
			{{-1, REDUCE, 4}, },
			{{-1, ERROR, 3}, {0, SHIFT, 1}, {2, SHIFT, 2}, {3, SHIFT, 3}, },
			{{-1, ERROR, 4}, {5, ACCEPT, -1}, },
			{{-1, REDUCE, 1}, {2, SHIFT, 2}, {3, SHIFT, 3}, },
			{{-1, REDUCE, 3}, },
			{{-1, ERROR, 7}, {0, SHIFT, 1}, {2, SHIFT, 2}, {3, SHIFT, 3}, },
			{{-1, ERROR, 8}, {1, SHIFT, 7}, {2, SHIFT, 8}, },
			{{-1, REDUCE, 0}, },
			{{-1, ERROR, 10}, {4, SHIFT, 14}, },
			{{-1, REDUCE, 2}, },
			{{-1, REDUCE, 7}, },
			{{-1, REDUCE, 6}, },
			{{-1, REDUCE, 5}, },
        };*/
    private static int[][][] gotoTable;
/*      {
			{{-1, 4}, {3, 10}, {7, 12}, },
			{{-1, 5}, },
			{{-1, 6}, {5, 11}, },
			{{-1, 9}, {8, 13}, },
        };*/
    private static String[] errorMessages;
/*      {
			"expecting: head, variable, '('",
			"expecting: body, variable",
			"expecting: variable, '(', ')', EOF",
			"expecting: EOF",
			"expecting: ')', EOF",
			"expecting: ')'",
        };*/
    private static int[] errors;
/*      {
			0, 1, 2, 0, 3, 2, 2, 0, 1, 4, 5, 2, 4, 4, 2, 
        };*/
}
