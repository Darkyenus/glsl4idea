
/* Fragment Shader output variables*/
vec4 gl_FragCoord;
bool gl_FrontFacing;
vec4 gl_FragColor;
vec4 gl_FragData[gl_MaxDrawBuffers];
float gl_FragDepth;


/* Fragment shader readable varying */
varying vec4 gl_Color;
varying vec4 gl_SecondaryColor;
varying vec4 gl_TexCoord[];         // at most will be gl_MaxTextureCoords
varying float gl_FogFragCoord;
varying vec2 gl_PointCoord;

/*
 * Returns the derivative in x using local differencing for the input argument p.
 *
 * @param p
 * @return
 */
genType dFdx (genType p);

/*
 * Returns the derivative in y using local differencing for  the input argument p.
 * These two functions are commonly used to estimate the filter width used to anti-alias procedural textures. We
 * are assuming that the expression is being evaluated in parallel on a SIMD array so that at any given point in
 * time the value of the function is known at the grid points represented by the SIMD array. Local differencing
 * between SIMD array elements can therefore be used to derive dFdx, dFdy, etc.
 *
 * @param p
 * @return
 */
genType dFdy (genType p);

/*
 * Returns the sum of the absolute derivative in x and y using local differencing for the input argument p, i.e.:
 * abs (dFdx (p)) + abs (dFdy (p));
 *
 * @param p
 * @return
 */
genType fwidth (genType p); 
