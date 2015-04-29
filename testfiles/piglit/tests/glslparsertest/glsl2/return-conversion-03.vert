/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The conversions in the table above are done only as indicated
 *     by other sections of this specification."
 *
 * Neither section 6.1 (Function Definitions) nor section 6.4 (Jumps) make any
 * mention of implicit conversions occuring on the expression of a return
 * statement.
 */
#version 120

float function(int i)
{
   return i;
}
