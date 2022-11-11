//
// GLSL vertex shader builtins
// first introduced in GLSL 110
// see https://www.opengl.org/registry/doc/GLSLangSpec.4.50.pdf § 7.1
// see https://www.opengl.org/registry/doc/GLSLangSpec.Full.1.120.8.pdf § 7.3, 7.6
//

#if __VERSION__ <= 120
attribute vec4  gl_Color;
attribute vec4  gl_SecondaryColor;
attribute vec3  gl_Normal;
attribute vec4  gl_Vertex;
attribute vec4  gl_MultiTexCoord0;
attribute vec4  gl_MultiTexCoord1;
attribute vec4  gl_MultiTexCoord2;
attribute vec4  gl_MultiTexCoord3;
attribute vec4  gl_MultiTexCoord4;
attribute vec4  gl_MultiTexCoord5;
attribute vec4  gl_MultiTexCoord6;
attribute vec4  gl_MultiTexCoord7;
attribute float gl_FogCoord;

varying vec4  gl_FrontColor;
varying vec4  gl_BackColor;
varying vec4  gl_FrontSecondaryColor;
varying vec4  gl_BackSecondaryColor;
varying vec4  gl_TexCoord[];
varying float gl_FogFragCoord;
#else

in  int   gl_VertexID;

#if __VERSION__ >= 140
in  int   gl_InstanceID;
#endif

out gl_PerVertex {
    vec4  gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[];
    #if __VERSION__ >= 450
    float gl_CullDistance[];
    #endif
};

#endif
