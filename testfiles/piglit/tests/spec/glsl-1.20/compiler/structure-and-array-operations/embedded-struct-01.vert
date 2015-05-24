/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From section 4.1.8 ("Structures") of the GLSL 1.20 spec:
 *
 *     "Anonymous structures are not supported. Embedded structures
 *     are not supported.
 *
 *         struct S { float f; };
 *
 *         struct T {
 *             S;              // Error: anonymous structures disallowed
 *             struct { ... }; // Error: embedded structures disallowed
 *             S s;            // Okay: nested structures with name are allowed
 *         };"
 */
#version 120

struct T {
    struct { float f; } s; // Error: embedded structures disallowed
};

void main()
{
    gl_Position = vec4(1);
}
