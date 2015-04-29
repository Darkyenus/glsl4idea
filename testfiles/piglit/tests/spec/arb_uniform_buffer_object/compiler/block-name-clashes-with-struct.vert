/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_uniform_buffer_object
 * [end config]
 *
 * Page 31 (page 37 of the PDF) of the GLSL 1.40 spec says:
 *
 *     "Uniform block names and variable names declared within uniform blocks
 *     are scoped at the program level."
 */
#version 120
#extension GL_ARB_uniform_buffer_object: require

uniform a {
  vec4 b;
};

struct a {
  vec4 c;
};

void main()
{
  gl_Position = b;
}
