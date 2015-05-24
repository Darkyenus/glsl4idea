/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

/* The ARB_shading_language_420pack spec says:
 *     "The *length* method may be applied to vectors (but not scalars)."
 *
 * Verify that scalar.length() generates an error.
 */

void main() {
    float x;
    gl_FragColor = vec4(x.length());
}
