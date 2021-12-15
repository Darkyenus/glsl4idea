//
// GLSL geometry shader builtins (first in GLSL 150)
// see https://www.opengl.org/registry/doc/GLSLangSpec.4.50.pdf § 7.1
//

in gl_PerVertex {
    vec4  gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    #if __VERSION__ >= 450
    float gl_CullDistance[];
    #endif
} gl_in[];

in int gl_PrimitiveIDIn;
in int gl_InvocationID;

out gl_PerVertex {
    vec4  gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    #if __VERSION__ >= 450
    float gl_CullDistance[];
    #endif
};

out int gl_PrimitiveID;
out int gl_Layer;
out int gl_ViewportIndex;
