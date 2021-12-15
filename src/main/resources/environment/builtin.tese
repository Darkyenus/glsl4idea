//
// GLSL tessellation evaluation shader builtins (first in GLSL 400)
// see https://www.opengl.org/registry/doc/GLSLangSpec.4.50.pdf § 7.1
//

in gl_PerVertex {
    vec4  gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    #if __VERSION__ >= 450
    float gl_CullDistance[];
    #endif
} gl_in[gl_MaxPatchVertices];

in int gl_PatchVerticesIn;
in int gl_PrimitiveID;
in vec3 gl_TessCoord;
patch in float gl_TessLevelOuter[4];
patch in float gl_TessLevelInner[2];

out gl_PerVertex {
    vec4  gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    #if __VERSION__ >= 450
    float gl_CullDistance[];
    #endif
};
