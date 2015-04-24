//
// GLSL uniform builtins
// see https://www.opengl.org/registry/doc/GLSLangSpec.4.50.pdf § 7.4
// TODO: this is just what GLSL 450 contains, previous versions don't have all of these
//

struct gl_DepthRangeParameters {
    float near;
    float far;
    float diff;
};
uniform gl_DepthRangeParameters gl_DepthRange;

uniform int gl_NumSamples;
