/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since c is a 3-column matrix, but 2 columns given as initializer
    mat3x3 c = { vec3(0.0), vec3(1.0)  };
    gl_FragColor = vec4(1.0);
}
