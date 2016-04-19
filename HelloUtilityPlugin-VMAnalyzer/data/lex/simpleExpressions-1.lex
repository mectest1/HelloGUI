/*@2016-04-17*/
/*ref Fig 3.23 from Purple Dragon Book, page 143*/

%{
	/*Definitions of manifest constants:
	LT, LE, EQ, NE, GE,
	IF, THEN, ELSE, ID, NUMBER, RELOP*/
%}
/*Part 1: declarations
%%
Part 2: translation rules
%%
Part 3: auxiliary functions*/

/*regular definitions*/
delim		[ \t\n]
ws			{delim}+
letter		[A-Za-z_]
digit		[0-9]
id			{letter}({letter}|{digit})*
number		{digit}+(\.{digit}+)?(E[+-]?{digit}+)?

%%
{ws}		{/*no action and no return*/}
if			{return (IF);}
then 		{return (THEN);}
else		{return (ELSE);}
{id}		{yylval = (int) installID(); return (ID);}
{number}	{yylval = (int) installNum(); return (NUMBER);}
"<"			{yylval = LT; return (RELOP);}
"<="		{yylval = LE; return (RELOP);}
"="			{yylval = EQ; return (RELOP);}
"<>"		{yylval = NE; return (RELOP);}
">"			{yylval = GT; return (RELOP);}
">="		{yylval = GE; return (RELOP);}


%%
int installID(){
	/*
	function to install the lexme, whose first character is pointed to 
	by yytext, and whose length is yyleng, into the symbol table and 
	return a pointer thereto
	*/
	return 0;	//dummy return value
}

int installNum(){
	/*
	similar to installID(), but puts numberical constaints into a 
	separate table
	*/
	return 0;	//dummy return value
}







































