
/* Vertex Shader output variables */
vec4 gl_Position; // must be written to
float gl_PointSize; // may be written to
vec4 gl_ClipVertex; // may be written to

//
// Vertex Attributes, p. 19.
//
attribute vec4 gl_Color;
attribute vec4 gl_SecondaryColor;
attribute vec3 gl_Normal;
attribute vec4 gl_Vertex;
attribute vec4 gl_MultiTexCoord0;
attribute vec4 gl_MultiTexCoord1;
attribute vec4 gl_MultiTexCoord2;
attribute vec4 gl_MultiTexCoord3;
attribute vec4 gl_MultiTexCoord4;
attribute vec4 gl_MultiTexCoord5;
attribute vec4 gl_MultiTexCoord6;
attribute vec4 gl_MultiTexCoord7;
attribute float gl_FogCoord;


/* Vertex Shader writable varying */
varying vec4 gl_FrontColor;
varying vec4 gl_BackColor;
varying vec4 gl_FrontSecondaryColor;
varying vec4 gl_BackSecondaryColor;
varying vec4 gl_TexCoord[];         // at most will be gl_MaxTextureCoords
varying float gl_FogFragCoord;

/*
 * For vertex shaders only. This function will ensure that the incoming vertex value will be transformed in a way
 * that produces exactly the same result as would be produced by OpenGL’s fixed functionality transform. It
 * is intended to be used to compute gl_Position, e.g.,
 *      gl_Position = ftransform()
 * This function should be used, for example, when an application is rendering the same geometry in separate
 * passes, and one pass uses the fixed functionality path to render and another pass uses programmable shaders.
 *
 * @return fixed pipeline transformed vertex 
 */
vec4 ftransform();

