/*
 *     Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
 *
 *     This file is part of glsl4idea.
 *
 *     Glsl4idea is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     Glsl4idea is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with glsl4idea.  If not, see <http://www.gnu.org/licenses/>.
 */
package glslplugin.lang.scanner;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static glslplugin.lang.elements.GLSLTokenTypes.*;

%%

%class GLSLFlexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state PREPROCESSOR
%state PREPROCESSOR_RAW_MODE

%{
%}


DIGIT               = [0-9]
OCTAL_DIGIT         = [0-7]
HEX_DIGIT           = [0-9A-Fa-f]
NON_DIGIT           = [_a-zA-Z]

LINE_TERMINATOR     = \r|\n|\r\n
WHITE_SPACE         = [ \t\f]

IDENTIFIER          = {NON_DIGIT}({NON_DIGIT} | {DIGIT})*

UINT_SUFFIX      = [Uu]
INTEGER_CONSTANT    = ({DECIMAL_CONSTANT} | {HEX_CONSTANT} | {OCTAL_CONSTANT})
DECIMAL_CONSTANT    = (0|([1-9]({DIGIT})*))
HEX_CONSTANT        = 0[Xx]({HEX_DIGIT})*
OCTAL_CONSTANT      = 0({OCTAL_DIGIT})*

DOUBLE_SUFFIX = (lf|LF)
FLOAT_SUFFIX = [fF]
FLOATING_CONSTANT   = (({FLOATING_CONSTANT1})|({FLOATING_CONSTANT2})|({FLOATING_CONSTANT3})|({FLOATING_CONSTANT4}))
FLOATING_CONSTANT1  = ({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
FLOATING_CONSTANT2  = "."({DIGIT})+({EXPONENT_PART})?
FLOATING_CONSTANT3  = ({DIGIT})+({EXPONENT_PART})
FLOATING_CONSTANT4  = ({DIGIT})+
EXPONENT_PART       = [Ee]["+""-"]?({DIGIT})*

LINE_COMMENT        = "//"[^\r\n]*
BLOCK_COMMENT       = "/*"([^"*"]|("*"+[^"*""/"]))*("*"+"/")?

%%


/**
 * LEXICAL RULES:
 */

\\{LINE_TERMINATOR}     { return WHITE_SPACE; }

 /* Preprocessor rules */
<PREPROCESSOR> {
  {WHITE_SPACE}+        { return WHITE_SPACE; }
  {LINE_TERMINATOR}     { yybegin(YYINITIAL); return PREPROCESSOR_END; }
  \"([^\"\r\n])*\"      { return PREPROCESSOR_STRING; }
  define                { return PREPROCESSOR_DEFINE; }
  undef                 { return PREPROCESSOR_UNDEF; }
  if                    { return PREPROCESSOR_IF; }
  ifdef                 { return PREPROCESSOR_IFDEF; }
  ifndef                { return PREPROCESSOR_IFNDEF; }
  else                  { return PREPROCESSOR_ELSE; }
  elif                  { return PREPROCESSOR_ELIF; }
  endif                 { return PREPROCESSOR_ENDIF; }
  error                 { return PREPROCESSOR_ERROR; }
  pragma                { yybegin(PREPROCESSOR_RAW_MODE); return PREPROCESSOR_PRAGMA; }
  extension             { yybegin(PREPROCESSOR_RAW_MODE); return PREPROCESSOR_EXTENSION; }
  version               { yybegin(PREPROCESSOR_RAW_MODE); return PREPROCESSOR_VERSION; }
  line                  { return PREPROCESSOR_LINE; }
  defined               { return PREPROCESSOR_DEFINED; }
  ##                    { return PREPROCESSOR_CONCAT; }
}

<PREPROCESSOR_RAW_MODE> {
  {LINE_TERMINATOR}     { yybegin(YYINITIAL); return PREPROCESSOR_END; }
  (\\?[^\\\r\n])*       { return PREPROCESSOR_RAW; }
}

<YYINITIAL> {
  #                     { yybegin(PREPROCESSOR); return PREPROCESSOR_BEGIN; }
  ({WHITE_SPACE}|{LINE_TERMINATOR})+ {return WHITE_SPACE;}
}

true                    {return BOOL_CONSTANT; }
false                   {return BOOL_CONSTANT; }

/* GLSL types */
void                    {return VOID_TYPE; }
float                   {return FLOAT_TYPE; }
double                  {return DOUBLE_TYPE; }
int                     {return INT_TYPE; }
uint                    {return UINT_TYPE; }
bool                    {return BOOL_TYPE; }
vec2                    {return VEC2_TYPE; }
vec3                    {return VEC3_TYPE; }
vec4                    {return VEC4_TYPE; }
dvec2                   {return DVEC2_TYPE; }
dvec3                   {return DVEC3_TYPE; }
dvec4                   {return DVEC4_TYPE; }
ivec2                   {return IVEC2_TYPE; }
ivec3                   {return IVEC3_TYPE; }
ivec4                   {return IVEC4_TYPE; }
uvec2                   {return UVEC2_TYPE; }
uvec3                   {return UVEC3_TYPE; }
uvec4                   {return UVEC4_TYPE; }
bvec2                   {return BVEC2_TYPE; }
bvec3                   {return BVEC3_TYPE; }
bvec4                   {return BVEC4_TYPE; }
mat2                    {return MAT2_TYPE; }
mat3                    {return MAT3_TYPE; }
mat4                    {return MAT4_TYPE; }
mat2x2                  {return MAT2X2_TYPE; }
mat2x3                  {return MAT2X3_TYPE; }
mat2x4                  {return MAT2X4_TYPE; }
mat3x2                  {return MAT3X2_TYPE; }
mat3x3                  {return MAT3X3_TYPE; }
mat3x4                  {return MAT3X4_TYPE; }
mat4x2                  {return MAT4X2_TYPE; }
mat4x3                  {return MAT4X3_TYPE; }
mat4x4                  {return MAT4X4_TYPE; }
dmat2                   {return DMAT2_TYPE; }
dmat3                   {return DMAT3_TYPE; }
dmat4                   {return DMAT4_TYPE; }
dmat2x2                 {return DMAT2X2_TYPE; }
dmat2x3                 {return DMAT2X3_TYPE; }
dmat2x4                 {return DMAT2X4_TYPE; }
dmat3x2                 {return DMAT3X2_TYPE; }
dmat3x3                 {return DMAT3X3_TYPE; }
dmat3x4                 {return DMAT3X4_TYPE; }
dmat4x2                 {return DMAT4X2_TYPE; }
dmat4x3                 {return DMAT4X3_TYPE; }
dmat4x4                 {return DMAT4X4_TYPE; }
atomic_uint             {return ATOMIC_UINT_TYPE; }

[iu]?(sampler|image)(1D|2D|3D|Cube|2DRect|1DArray|2DArray|Buffer|2DMS|2DMSArray|CubeArray) { return SAMPLER_TYPE; }
sampler(1D|2D|Cube|2DRect|1DArray|2DArray|CubeArray)Shadow { return SAMPLER_TYPE; }


/* GLSL STORAGE QUALIFIERS */
//note: these are declared separately to better support error handling
//      for example swapping varying and centroid should not cause lexer failure.
//      nor should the "wrong" whitespace separation do.
const                   {return CONST_KEYWORD; }
attribute               {return ATTRIBUTE_KEYWORD; }
uniform                 {return UNIFORM_KEYWORD; }
varying                 {return VARYING_KEYWORD; }
centroid                {return CENTROID_KEYWORD; }
invariant               {return INVARIANT_KEYWORD; }
patch                   {return PATCH_KEYWORD; }
sample                  {return SAMPLE_KEYWORD; }
buffer                  {return BUFFER_KEYWORD; }
shared                  {return SHARED_KEYWORD; }
coherent                {return COHERENT_KEYWORD; }
volatile                {return VOLATILE_KEYWORD; }
restrict                {return RESTRICT_KEYWORD; }
readonly                {return READONLY_KEYWORD; }
writeonly               {return WRITEONLY_KEYWORD; }
subroutine              {return SUBROUTINE_KEYWORD; }
precise                 {return PRECISE_KEYWORD; }
layout                  {return LAYOUT_KEYWORD; }

/* GLSL INTERPOLATION QUALIFIERS */

smooth                  {return SMOOTH_KEYWORD; }
flat                    {return FLAT_KEYWORD; }
noperspective           {return NOPERSPECTIVE_KEYWORD; }

/* GLSL ES STORAGE QUALIFIERS */
highp                   {return HIGHP_KEYWORD; }
mediump                 {return MEDIUMP_KEYWORD; }
lowp                    {return LOWP_KEYWORD; }

/* GLSL PARAMETER QUALIFIER */
in                      {return IN_KEYWORD; }
out                     {return OUT_KEYWORD; }
inout                   {return INOUT_KEYWORD; }


/* GLSL ITERATION FLOW_KEYWORDS */
while                   {return WHILE_KEYWORD; }
do                      {return DO_KEYWORD; }
for                     {return FOR_KEYWORD; }

/* GLSL JUMP FLOW_KEYWORDS */
break                   {return BREAK_JUMP_STATEMENT; }
continue                {return CONTINUE_JUMP_STATEMENT; }
return                  {return RETURN_JUMP_STATEMENT; }
discard                 {return DISCARD_JUMP_STATEMENT; }

struct                  {return STRUCT; }

/* GLSL SELECTION FLOW_KEYWORDS */
if                      {return IF_KEYWORD; }
else                    {return ELSE_KEYWORD; }
switch                  {return SWITCH_KEYWORD; }
case                    {return CASE_KEYWORD; }
default                 {return DEFAULT_KEYWORD; }

/* GLSL ES PRECISION */
precision  {return PRECISION_KEYWORD; }

/* GLSL reserved keywords */
common { return RESERVED_KEYWORD; }
partition { return RESERVED_KEYWORD; }
active { return RESERVED_KEYWORD; }
asm { return RESERVED_KEYWORD; }
class { return RESERVED_KEYWORD; }
union { return RESERVED_KEYWORD; }
enum { return RESERVED_KEYWORD; }
typedef { return RESERVED_KEYWORD; }
template { return RESERVED_KEYWORD; }
this { return RESERVED_KEYWORD; }
resource { return RESERVED_KEYWORD; }
goto { return RESERVED_KEYWORD; }
inline { return RESERVED_KEYWORD; }
noinline { return RESERVED_KEYWORD; }
public { return RESERVED_KEYWORD; }
static { return RESERVED_KEYWORD; }
extern { return RESERVED_KEYWORD; }
external { return RESERVED_KEYWORD; }
interface { return RESERVED_KEYWORD; }
long { return RESERVED_KEYWORD; }
short { return RESERVED_KEYWORD; }
half { return RESERVED_KEYWORD; }
fixed { return RESERVED_KEYWORD; }
unsigned { return RESERVED_KEYWORD; }
superp { return RESERVED_KEYWORD; }
input { return RESERVED_KEYWORD; }
output { return RESERVED_KEYWORD; }
[hf]vec[234] { return RESERVED_KEYWORD; }
sampler3DRect { return RESERVED_KEYWORD; }
filter { return RESERVED_KEYWORD; }
sizeof { return RESERVED_KEYWORD; }
cast { return RESERVED_KEYWORD; }
namespace { return RESERVED_KEYWORD; }
using { return RESERVED_KEYWORD; }


/* GLSL Symbols */
"{"                     {return LEFT_BRACE; }
"}"                     {return RIGHT_BRACE; }
"["                     {return LEFT_BRACKET; }
"]"                     {return RIGHT_BRACKET; }
"("                     {return LEFT_PAREN; }
")"                     {return RIGHT_PAREN; }

"="                     {return EQUAL; }

"*="                    {return MUL_ASSIGN; }
"*"                     {return STAR; }
"/="                    {return DIV_ASSIGN; }
"/"                     {return SLASH; }
"+="                    {return ADD_ASSIGN; }
"+"                     {return PLUS; }
"-="                    {return SUB_ASSIGN; }
"-"                     {return DASH; }
"%="                    {return MOD_ASSIGN; }
"%"                     {return PERCENT; }
"<<="                   {return LEFT_ASSIGN; }
"<<"                    {return LEFT_OP; }
">>="                   {return RIGHT_ASSIGN; }
">>"                    {return RIGHT_OP; }
"&="                    {return AND_ASSIGN; }
"&"                     {return AMPERSAND; }
"^="                    {return XOR_ASSIGN; }
"^"                     {return CARET; }
"|="                    {return OR_ASSIGN; }
"|"                     {return VERTICAL_BAR; }

"~"                     {return TILDE; }
"--"                    {return DEC_OP; }
"++"                    {return INC_OP; }

"=="                    {return EQ_OP; }
"<"                     {return LEFT_ANGLE; }
">"                     {return RIGHT_ANGLE; }
">="                    {return GE_OP; }
"<="                    {return LE_OP; }
"!="                    {return NE_OP; }
"&&"                    {return AND_OP; }
"||"                    {return OR_OP; }
"^^"                    {return XOR_OP; }

"?"                     {return QUESTION; }
":"                     {return COLON; }
"!"                     {return BANG; }
"."                     {return DOT; }
";"                     {return SEMICOLON; }
","                     {return COMMA; }

{IDENTIFIER}            {return IDENTIFIER;}

{INTEGER_CONSTANT}{UINT_SUFFIX} {return UINT_CONSTANT; }
{INTEGER_CONSTANT}      {return INTEGER_CONSTANT; }
{FLOATING_CONSTANT}{DOUBLE_SUFFIX}     {return DOUBLE_CONSTANT; }
{FLOATING_CONSTANT}{FLOAT_SUFFIX}?     {return FLOAT_CONSTANT; }
{LINE_COMMENT}          {return COMMENT_LINE; }
{BLOCK_COMMENT}         {return COMMENT_BLOCK; }
.                       {return UNKNOWN; }
