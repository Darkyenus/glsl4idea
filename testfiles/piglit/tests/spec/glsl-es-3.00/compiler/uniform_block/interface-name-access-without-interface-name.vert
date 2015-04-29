/* [config]
 * expect_result: fail
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 *     "If an instance name (instance-name) is used, then it puts all the
 *     members inside a scope within its own name space, accessed with the
 *     field selector ( . ) operator (analogously to structures)."
 *
 * Accesses to a field of a uniform block that was declared with an instance
 * name therefore must use that instance name.  Otherwise an error should be
 * generated.
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
} camera;

in vec4 position;

void main()
{
  gl_Position = mvp * position;
}
