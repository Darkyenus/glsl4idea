/* [config]
 * expect_result: fail
 * glsl_version: 1.00
 * [end config]
 *
 * From section 4.1.8 ("Structures") of the GLSL ES 1.00 spec:
 *
 *     "Anonymous structure declarators (member declaration whose type is a
 *     structure but has no declarator) are not supported.
 *
 *         struct S
 *         {
 *             int x;
 *         };
 *
 *         struct T
 *         {
 *             S;
 *             int y;
 *         };"
 */
#version 100

struct S
{
    int x;
};

struct T
{
    S;
    int y;
};

void main()
{
    gl_Position = vec4(1);
}
