/*

Britney - A simple untyped Lambda Calculus interpreter

This file is part of Britney.
See the file "LICENSE" for copyright information and the
terms and conditions for copying, distribution and
modification of Britney.

*/


package nl.vu.cs.mvermaat.britney;


import nl.vu.cs.mvermaat.britney.grammar.parser.*;
import nl.vu.cs.mvermaat.britney.grammar.lexer.*;
import nl.vu.cs.mvermaat.britney.grammar.node.*;
import java.io.*;
import java.util.*;


public class Britney {


    public static final String
        AUTHOR = "Martijn Vermaat (mvermaat@cs.vu.nl)",
        VERSION = "0.3",
        BUILD_DATE = "2004/08/09";

    private final static int MAX_TRIES_NORMALIZE = 4000,
                             MAX_LINES_TERMINAL = 18;


    private PrintWriter out;
    private PrintWriter errorOut;
    private BufferedReader in;

    private Start currentTerm;
    private HashMap state;
    private NameGenerator generator;

    private boolean run;
    private boolean silent;


    public static void main(String[] arguments) {

        new Britney().start(arguments);

    }


    private void start(String[] arguments) {

        silent = false;

        out = new PrintWriter(System.out, true);
        errorOut = new PrintWriter(System.out, true);

        if (arguments.length > 0) {

            if (ARGUMENT_INFO.equals(arguments[0])) {
                printLine(MESSAGE_INFO);
                return;
            } else if (ARGUMENT_HELP.equals(arguments[0])) {
                printLine(MESSAGE_START);
                printLine("");
                printLine(MESSAGE_HELP);
                return;
            } else if (ARGUMENT_LICENSE.equals(arguments[0])) {
                printLine(MESSAGE_LICENSE);
                return;
            }

        }

        printLine(MESSAGE_START);

        currentTerm = null;
        state = new HashMap();
        generator = new NameGenerator();

        for (int i=0; i<arguments.length; i++) {

            try {
                in = new BufferedReader(new FileReader(arguments[i]));
            } catch (FileNotFoundException e) {
                printError(ERROR_FILE_NOT_FOUND + arguments[i]);
                continue;
            }

            silent = false;

            printLine(MESSAGE_FILE_LOADED + arguments[i]);

            silent = true;
            run();

        }

        currentTerm = null;

        silent = false;

        in = new BufferedReader(new InputStreamReader(System.in));

        run();

    }


    private void run() {

        run = true;

        String action;

        while (run) {

            printString(COMMAND_STRING);

            action = null;

            try {
                action = in.readLine();
            } catch (IOException e) {
                printError(e.getMessage());
                System.exit(1);
            }

            if (action == null) {
                printLine("");
                action = ACTION_QUIT;
            }

            action = action.trim();

            if (action.equals("")) {
                continue;
            }

            if (action.equals(ACTION_QUIT)) {
                actionQuit();
            } else if (action.startsWith(ACTION_COMMENT)) {
                actionComment();
            } else if (action.equals(ACTION_USE)) {
                actionUse();
            } else if (action.equals(ACTION_STORE)) {
                actionStore();
            } else if (action.equals(ACTION_STORED)) {
                actionStored();
            } else if (action.equals(ACTION_PRINT)) {
                actionPrint();
            } else if (action.equals(ACTION_STRUCTURE)) {
                actionStructure();
            } else if (action.equals(ACTION_AST)) {
                actionAst();
            } else if (action.equals(ACTION_REDUCE)) {
                actionReduce();
            } else if (action.equals(ACTION_NORMALIZE)) {
                actionNormalize();
            } else if (action.equals(ACTION_MATCH)) {
                actionMatch();
            } else if (action.equals(ACTION_HELP)) {
                actionHelp();
            } else if (action.equals(ACTION_INFO)) {
                actionInfo();
            } else {
                actionUnknownCommand();
            }

        }

    }


    private void actionQuit() {

        printLine(MESSAGE_QUIT);
        run = false;

    }


    private void actionHelp() {

        printPerPage(MESSAGE_HELP);

    }


    private void actionInfo() {

        printPerPage(MESSAGE_INFO);

    }


    private void actionComment() {

        // Finally, some time to relax

    }


    private void actionUnknownCommand() {

        printError(ERROR_UNKNOWN_COMMAND);

    }


    private void actionUse() {

        String use = null;

        printString(COMMAND_USE_TERM);

        try {
            use = in.readLine();
        } catch (IOException e) {
            printError(e.getMessage());
        }

        if (use != null) {

            Lexer l;
            Parser p;

            try {

                l = new Lexer(new PushbackReader(new StringReader(use)));
                p = new Parser(l);
                currentTerm = p.parse();

            } catch (Exception e) {
                printError(e.getMessage());
            }

            if (currentTerm != null) {

                substituteNames(currentTerm, state);
                actionPrint();

            }

        } else {
            printError(ERROR_INVALID_TERM);
        }

    }


    private void actionStore() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        printString(COMMAND_STORE_NAME);

        String name = null;

        try {
            name = in.readLine();
        } catch (IOException e) {
            printError(e.getMessage());
        }

        if (name != null) {

            name = name.trim();

            if (isValidName(name)) {
                state.put(name, currentTerm.clone());
            } else {
                printError(ERROR_INVALID_NAME);
            }

        } else {

            printError(ERROR_INVALID_NAME);

        }

    }


    private void actionStored() {

        Set names = state.keySet();
        String result = "";

        for (Iterator i = names.iterator(); i.hasNext();) {
            result += MESSAGE_STORED_TERM + ((String) i.next()) + "\n";
        }

        printPerPage(result);

    }


    private void actionPrint() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        StringWriter s = new StringWriter();

        currentTerm.apply(new PrettyPrinter(new PrintWriter(s)));

        printPerPage(MESSAGE_CURRENT_TERM + s.getBuffer().toString());

    }


    private void actionStructure() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        StringWriter s = new StringWriter();

        currentTerm.apply(new StructurePrinter(new PrintWriter(s)));

        printPerPage(MESSAGE_CURRENT_TERM + s.getBuffer().toString());

    }


    private void actionAst() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        StringWriter s = new StringWriter();

        currentTerm.apply(new ASTPrinter(new PrintWriter(s)));

        printPerPage(s.getBuffer().toString());

    }


    private void actionReduce() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        BetaReduction b = new BetaReduction(generator);

        currentTerm.apply(b);

        if (b.hasReduced()) {
            actionPrint();
        } else {
            printLine(MESSAGE_REDUCE_NORMAL_FORM);
        }

    }


    private void actionNormalize() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        BetaReduction b;

        for (int i=0; i<MAX_TRIES_NORMALIZE; i++) {

            b = new BetaReduction(generator);

            currentTerm.apply(b);

            if (!b.hasReduced()) {
                printLine(MESSAGE_NUM_REDUCTIONS + i);
                actionPrint();
                return;
            }

        }

        printLine(MESSAGE_NUM_REDUCTIONS + MAX_TRIES_NORMALIZE);
        printError(ERROR_NO_NORMAL_FORM);
        actionPrint();

    }


    private void actionMatch() {

        if (currentTerm == null) {
            printError(ERROR_NO_CURRENT_TERM);
            return;
        }

        Hashing h = new Hashing();
        currentTerm.apply(h);

        Hashing compare;

        Set names = state.keySet();

        for (Iterator i = names.iterator(); i.hasNext();) {

            String name = (String) i.next();

            PTerm node = ((Start)state.get(name)).getPTerm();

            compare = new Hashing();

            node.apply(compare);

            if (compare.getHash().equals(h.getHash())) {
                printLine(MESSAGE_MATCH_FOUND + name);
            }

        }

    }


    private void substituteNames(Start term, HashMap state) {

        Set names = state.keySet();

        for (Iterator i = names.iterator(); i.hasNext();) {

            String name = (String) i.next();

            PTerm node = ((Start)state.get(name)).getPTerm();

            term.apply(new Substitution(node, name, generator));

        }

    }


    private boolean isValidName(String name) {

        for (int i=0; i<name.length(); i++) {
            if (!isLetter(name.charAt(i))
                && !isDigit(name.charAt(i))) {
                return false;
            }
        }

        return true;

    }


    private boolean isLetter(char c) {

        return ((c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z'));

    }


    private boolean isDigit(char c) {

        return (c >= '0' && c <= '9');

    }


    private void printPerPage(String s) {

        String[] lines = s.split("\n");
        String enter;
        int i = 0;

        while (i < lines.length) {

            for (int j=0; j<MAX_LINES_TERMINAL && i<lines.length; j++) {
                printLine(lines[i++]);
            }

            if (i >= lines.length) {
                break;
            }

            printLine("");
            printString(COMMAND_NEXT_PAGE);

            enter = null;
            try {
                enter = in.readLine();
            } catch (IOException e) {
                printError(e.getMessage());
                return;
            }

            if (enter == null) {
                printLine("");
                break;
            }
            if (enter.equals(ACTION_QUIT_PAGES)) {
                break;
            }

        }

    }


    private void printString(String s) {

        if (!silent) {
            out.print(s);
            out.flush();
        }

    }


    private void printLine(String s) {

        if (!silent) {
            out.println(s);
        }

    }


    private void printError(String s) {

        errorOut.println(s);

    }


    /*
      Below are only message strings
    */


    private static final String
        ACTION_QUIT = "exit",
        ACTION_USE = "use",
        ACTION_STORE = "store",
        ACTION_STORED = "stored",
        ACTION_PRINT = "print",
        ACTION_STRUCTURE = "structure",
        ACTION_AST = "tree",
        ACTION_REDUCE = "reduce",
        ACTION_NORMALIZE = "normalize",
        ACTION_MATCH = "match",
        ACTION_COMMENT = "--",
        ACTION_HELP = "help",
        ACTION_INFO = "info",
        ACTION_QUIT_PAGES = "q",

        COMMAND_STRING = "> ",
        COMMAND_USE_TERM = "Term: ",
        COMMAND_STORE_NAME = "Name: ",
        COMMAND_COMMENT = "Enter anything you like: ",
        COMMAND_NEXT_PAGE = "More ('"+ACTION_QUIT_PAGES+"' to quit): ",

        ARGUMENT_HELP = "-help",
        ARGUMENT_INFO = "-info",
        ARGUMENT_LICENSE = "-license",

        MESSAGE_START = "Welcome to Britney version " + VERSION + " (" + BUILD_DATE + ").\n"
                      + "Start with '-info' for more information.",
        MESSAGE_QUIT = "Bye\n",
        MESSAGE_CURRENT_TERM = "Current term: ",
        MESSAGE_NUM_REDUCTIONS = "Number of reductions: ",
        MESSAGE_REDUCE_NORMAL_FORM = "Current term is already in normal form.",
        MESSAGE_MATCH_FOUND = "Match: ",
        MESSAGE_FILE_LOADED = "File loaded: ",
        MESSAGE_STORED_TERM = "Stored: ",

        ERROR_FILE_NOT_FOUND = "File not found: ",
        ERROR_UNKNOWN_COMMAND = "Sorry, don't know that command.\nUse the '" + ACTION_HELP + "' command for help.",
        ERROR_NO_CURRENT_TERM = "No current term.",
        ERROR_INVALID_TERM = "Not a valid term.",
        ERROR_INVALID_NAME = "Not a valid name. Names can contain letters and digits.",
        ERROR_NO_NORMAL_FORM = "Term seems to have no normal form.",

MESSAGE_HELP = "Available commands and a short description:\n\n"
+ ACTION_USE + "\n"
+ "  Enter a lambda term to use as the current term.\n"
+ "  All actions will always be applied to the current term.\n\n"
+ ACTION_STORE + "\n"
+ "  Enter a name to store the current term.\n"
+ "  A possible term stored with the same name will be lost.\n\n"
+ ACTION_PRINT + "\n"
+ "  Pretty-print the current term.\n\n"
+ ACTION_STRUCTURE + "\n"
+ "  Pretty-print the current term with all parentheses.\n\n"
+ ACTION_AST + "\n"
+ "  Print the abstract syntax tree for the current term.\n\n"
+ ACTION_REDUCE + "\n"
+ "  Try to apply one Beta-reduction step to the current term.\n"
+ "  The resulting term will be the new current term.\n\n"
+ ACTION_NORMALIZE + "\n"
+ "  Apply Beta-reduction steps as long as possible.\n"
+ "  Maximum number of reductions is " + MAX_TRIES_NORMALIZE + ".\n"
+ "  The resulting term will be the new current term.\n\n"
+ ACTION_STORED + "\n"
+ "  List all names of stored terms.\n\n"
+ ACTION_MATCH + "\n"
+ "  The current term will be matched to all stored terms modulo\n  Alpha-conversion.\n\n"
+ ACTION_COMMENT + " [comment]\n"
+ "  Anything on the line will be ignored.\n"
+ "  Usefull to include comments in definition files.\n\n"
+ ACTION_QUIT + "\n"
+ "  Exit Britney.\n\n"
+ ACTION_INFO + "\n"
+ "  Print some information on Britney (version, credits, etc).\n\n"
+ "All command line arguments, if present, will be interpreted as\n"
+ "file names for definition files. All commands in these files\n"
+ "will be executed on startup.\n\n"
+ "Example usage:\n\n"
+ "  > use\n"
+ "  Term: \\x.x\n"
+ "  Current term: \\x.x\n"
+ "  > store\n"
+ "  Name: I\n"
+ "  > use\n"
+ "  Term: (\\y.y y) I\n"
+ "  Current term: (\\y.y y) \\x.x\n"
+ "  > normalize\n"
+ "  Number of reductions: 2\n"
+ "  Current term: \\x.x",

MESSAGE_INFO = "This is Britney version " + VERSION + " (" + BUILD_DATE + ")\n"
+ "by Martijn Vermaat <mvermaat@cs.vu.nl>\n\n"
+ "Licensed under the new BSD License.\n"
+ "http://opensource.org/licenses/bsd-license.php\n\n"
+ "The parser Britney uses to parse Lambda terms was built using\n"
+ "the excelent SableCC 3-beta.2 tool by Etienne Gagnon and others.\n"
+ "http://www.sablecc.org/\n\n"
+ "The code to print the abstract syntax tree of a term is based\n"
+ "on SableCC Display Tools v1.0 by Roger Keays.\n"
+ "http://www.ninthave.net/downloads/91\n\n"
+ "The reduction strategy used by Britney is normal order (also:\n"
+ "leftmost-outermost). This strategy is terminating, therefore\n"
+ "Britney will always find a normal form if one exists (if it does\n"
+ "not take too much reductions that is).\n\n"
+ "Britney has a number of limitations, most notably:\n\n"
+ "  Reducing large terms can result in a StackOverflowError exception\n"
+ "  because descending the tree is done using heavy recursion.\n\n"
+ "  Terms of the form 'A \\x.B' have to be entered as 'A (\\x.B)',\n"
+ "  even though they are unambiguous following general conventions.\n"
+ "  This is because of a grammar limitation that might be fixed in\n"
+ "  a future version of Britney.\n\n"
+ "  The implementation of Beta reduction is kept close to the\n"
+ "  mathematical definition of Beta reduction in order to keep the\n"
+ "  code as clean as possible. Therefore, should you care about\n"
+ "  that, performance is really bad.\n\n"
+ "For more information, examples, and source code, please refer to:\n"
+ "http://www.cs.vu.nl/~mvermaat/things/britney.xhtml\n\n"
+ "Start with '-license' for licensing information.",

MESSAGE_LICENSE = "Britney - A simple untyped Lambda Calculus interpreter\n\n"
+ "Copyright 2004 Martijn Vermaat <mvermaat@cs.vu.nl>\n"
+ "All rights reserved.\n\n"
+ "All sourcecode in this project written by me, Martijn Vermaat, is\n"
+ "licensed under the new BSD License, included below. Please refer to\n"
+ "SableCC licensing documentation concerning the SableCC generated Java\n"
+ "code.\n\n\n"
+ "Redistribution and use in source and binary forms, with or without\n"
+ "modification, are permitted provided that the following conditions are\n"
+ "met:\n\n"
+ "* Redistributions of source code must retain the above copyright notice,\n"
+ "  this list of conditions and the following disclaimer.\n"
+ "* Redistributions in binary form must reproduce the above copyright\n"
+ "  notice, this list of conditions and the following disclaimer in the\n"
+ "  documentation and/or other materials provided with the distribution.\n"
+ "* Neither the name of the author nor the names of its contributors may\n"
+ "  be used to endorse or promote products derived from this software\n"
+ "  without specific prior written permission.\n\n"
+ "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS\n"
+ "\"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED\n"
+ "TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A\n"
+ "PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER\n"
+ "OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,\n"
+ "EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,\n"
+ "PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR\n"
+ "PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF\n"
+ "LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n"
+ "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n"
+ "SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";


}
