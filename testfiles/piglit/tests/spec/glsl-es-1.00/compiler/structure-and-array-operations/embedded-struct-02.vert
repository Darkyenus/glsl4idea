/* [config]
 * expect_result: pass
 * glsl_version: 1.00
 * [end config]
 *
 * From section 4.1.8 ("Structures") of the GLSL ES 1.00 spec:
 *
 *     "Embedded structure definitions are not supported:
 *
 *     Example 1:
 *
 *         struct S
 *         {
 *             struct T // error: embedded structure definition is not supported.
 *             {
 *                 int a;
 *             } t;
 *             int b:
 *         };
 *         
 *     Example 2:
 *
 *         struct T
 *         {
 *             int a;
 *         };
 *
 *         struct S
 *         {
 *             T t;    // ok.
 *             int b;
 *         };"
 */
#version 100

struct T
{
    int a;
};

struct S
{
    T t;    // ok.
    int b;
};

void main()
{
    gl_Position = vec4(1);
}
