/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 54 (page 60 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The size [of gl_TexCoord] can be at most
 *     gl_MaxTextureCoords."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

varying vec4 gl_TexCoord[gl_MaxTextureCoords][1];

void main() { gl_Position = vec4(0.0); }
