# Mini Expression Compiler

A simple Java compiler component that processes arithmetic expressions through the core compilation phases: lexical analysis, parsing, AST construction, and evaluation.

## Project Summary

The compiler pipeline has five stages:

1. **Lexer** – converts the input string into a token stream (numbers, operators `+ - * /`, parentheses, and `++`/`--`)
2. **Parser** – recursive-descent parser that validates the grammar and builds an AST
3. **Tree printer** – displays the AST as a 2-D diagram in the console
4. **Evaluator** – traverses the AST and computes the result
5. **Trace output** – prints tokens, parse status, tree, and result for every expression

Supports: binary operators, parenthesised sub-expressions, unary minus (`-3`), and post-increment/decrement (`5++`, `8--`).

## Setup

```
MiniExpressionCompiler/
├── src/
│   ├── Token.java        – token type + value
│   ├── Lexer.java        – tokeniser
│   ├── ASTNode.java      – AST node (value, left, right)
│   ├── Parser.java       – recursive-descent parser
│   ├── Evaluator.java    – AST evaluator
│   ├── TreePrinter.java  – console tree renderer
│   └── Main.java         – entry point
└── README.md
```


## Example Outputs

### `(3+2)*5-1`
```
Tokens: [(, 3, +, 2, ), *, 5, -, 1]
Parse: Success
Parse Tree:
          -
       __/ \
      *     1
   __/ \
  +     5
 / \
3   2
Evaluation Result: 24
```

### `10*(6-3)+8/2`
```
Tokens: [10, *, (, 6, -, 3, ), +, 8, /, 2]
Parse: Success
Parse Tree:
          +
   ______/ \__
  *           /
 / \__       / \
10    -     8   2
     / \
    6   3
Evaluation Result: 34
```

### `5++`  (post-increment — tree suppressed)
```
Tokens: [5, ++]
Parse: Success
Evaluation Result: 6
```

### `3+*5`  (invalid input)
```
Tokens: [3, +, *, 5]
Parse: Failure — Unexpected token '*' at position 2
```

## Grammar

```
E  →  T ( ('+' | '-') T )*
T  →  F ( ('*' | '/') F )*
F  →  '(' E ')'
    | '-' F          unary minus
    | '+' F          unary plus (no-op)
    | NUMBER ['++' | '--']
```
