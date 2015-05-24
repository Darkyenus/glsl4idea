/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

/* The ARB_shading_language_420pack spec allows const-qualified variables to be
 * initialized with non-constant expressions in local scope, but it says:
 *
 *     "Initializers for *const* declarations at global scope must be
 *      constant expressions..."
 *
 * Verify that non-constant expressions may not be initializers of const-
 * qualified global variables.
 */

uniform float a_uniform;
const float a_var = a_uniform + 1.0;

void main() {
    gl_FragColor = vec4(a_var);
}
