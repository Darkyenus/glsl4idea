// gentype = float, vec2, vec3, or vec4

//*** Angle and Trigonometry Functions ***//
/*
 * Converts degrees to radians, i.e. (pi / 180) * degrees
 *
 * @param degrees
 * @return as radians
 */
genType radians(genType degrees);

/*
 * Converts radians to degrees, i.e.(180 / pi) * radians
 *
 * @param radians
 * @return as degrees
 */
genType degrees(genType radians);

/*
 * The standard trigonometric sine function.
 *
 * @param angle in radians
 * @return sine value
 */
genType sin(genType angle);

/*
 * The standard trigonometric cosine function.
 *
 * @param angle in radians
 * @return cosine value
 */
genType cos(genType angle);

/*
 * The standard trigonometric tangent.
 *
 * @param angle in radians
 * @return tangent value
 */
genType tan(genType angle);

/*
 * Arc sine. Returns an angle whose sine is x. The range of values returned by this
 * function is [?-pi/2, pi/2] Results are undefined if |x|>1.
 *
 * @param x sine value
 * @return angle of a sine value
 */
genType asin(genType x);

/*
 * Arc cosine. Returns an angle whose cosine is x. The range of values returned by this function is [0, p].
 * Results are undefined if |x|>1.
 *
 * @param x cosine value
 * @return angle of a cosine value
 */
genType acos (genType x);

/*
 * Arc tangent. Returns an angle whose tangent is y/x. The signs of x and y are used to determine what quadrant the
 * angle is in. The range of values returned by this function is [?pi,pi]. Results are undefined if x and y are both 0.
 *
 * @param x
 * @param y
 * @return angle
 */
genType atan (genType y, genType x);

/*
 * Arc tangent. Returns an angle whose tangent is y_over_x. The range of values returned by this function is [?pi/2, pi/2 ].
 *
 * @param y_over_x
 * @return angle
 */
genType atan (genType y_over_x);


//*** Exponential Functions ***//

/*
 * Returns x raised to the y power, i.e., x^y
 * Results are undefined if x < 0.
 * Results are undefined if x = 0 and y <= 0.
 *
 * @param x
 * @param y
 * @return x^y
 */
genType pow (genType x, genType y);

/*
 * Returns the natural exponentiation of x, i.e., e^x
 *
 * @param x
 * @return
 */
genType exp (genType x);

/*
 * Returns the natural logarithm of x, i.e., returns the value y which satisfies the equation x = e^y.
 * Results are undefined if x <= 0.
 *
 * @param x
 * @return
 */
genType log (genType x);

/*
 * Returns 2 raised to the x power, i.e., 2^x
 *
 * @param x
 * @return
 */
genType exp2 (genType x);

/*
 * Returns the base 2 logarithm of x, i.e., returns the value y which satisfies the equation x=2^y
 * Results are undefined if x <= 0.
 *
 * @param x
 * @return
 */
genType log2 (genType x);

/*
 * Returns the square root of x. Results are undefined if x < 0.
 *
 * @param x
 * @return
 */
genType sqrt (genType x);

/*
 * Returns  1/ sqrt(x) Results are undefined if x <= 0.
 *
 * @param x
 * @return
 */
genType inversesqrt (genType x);



//*** Common Functions ***//

/*
 * Returns x if x >= 0, otherwise it returns –x.
 *
 * @param x
 * @return
 */
genType abs (genType x);

/*
 * Returns 1.0 if x > 0, 0.0 if x = 0, or –1.0 if x < 0
 *
 * @param x
 * @return
 */
genType sign (genType x);

/*
 * Returns a value equal to the nearest integer that is less than or equal to x
 *
 * @param x
 * @return
 */
genType floor (genType x);

/*
 * Returns a value equal to the nearest integer that is greater than or equal to x
 *
 * @param x
 * @return
 */
genType ceil (genType x);

/*
 * Returns x – floor (x)
 *
 * @param x
 * @return
 */
genType fract (genType x);

/*
 * Modulus. Returns x – y * floor (x/y)
 *
 * @param x
 * @param y
 * @return
 */
genType mod (genType x, float y);

/*
 * Modulus. Returns x – y * floor (x/y)
 *
 * @param x
 * @param y
 * @return
 */
genType mod (genType x, genType y);

/*
 * Returns y if y < x, otherwise it returns x
 *
 * @param x
 * @param y
 * @return
 */
genType min (genType x, genType y);
genType min (genType x, float y);
//todo: repeat javadoc?

/*
 * Returns y if x < y, otherwise it returns x.
 *
 * @param x
 * @return
 */
genType max (genType x, genType y);
genType max (genType x, float y);

/*
 * Returns min (max (x, minVal), maxVal)
 * Results are undefined if minVal > maxVal.
 *
 * @param x
 * @return
 */
genType clamp (genType x, genType minVal, genType maxVal);
genType clamp (genType x, float minVal, float maxVal);

/*
 * Returns the linear blend of x and y, i.e. x * (1?a) + y * a
 *
 * @param x
 * @param y
 * @param a
 * @return
 */
genType mix (genType x, genType y, genType a);
genType mix (genType x, genType y, float a);

/*
 * Returns 0.0 if x < edge, otherwise it returns 1.0
 *
 * @param edge
 * @param x
 * @return
 */
genType step (genType edge, genType x);
genType step (float edge, genType x);

/*
 * Returns 0.0 if x <= edge0 and 1.0 if x >= edge1 and performs smooth Hermite interpolation between 0 and 1
 * when edge0 < x < edge1. This is useful in cases where you would want a threshold function with a smooth
 * transition. This is equivalent to:
 *
 *  genType t;
 *  t = clamp ((x – edge0) / (edge1 – edge0), 0, 1);
 *  return t * t * (3 – 2 * t);
 *
 *  Results are undefined if edge0 >= edge1
 *
 * @param edge0
 * @param edge1
 * @param x
 * @return
 */
genType smoothstep (genType edge0, genType edge1, genType x);
genType smoothstep (float edge0, float edge1, genType x);



//*** Geometric Functions ***//

/*
 * Returns the length of vector x, i.e., sqrt(x[0]^2+ x[1]^2...)
 *
 * @param x
 * @return
 */
float length (genType x);

/*
 * Returns the distance between p0 and p1, i.e. length (p0 – p1)
 *
 * @param p0
 * @param p1
 * @return
 */
float distance (genType p0, genType p1);

/*
 * Returns the dot product of x and y, i.e., x[0]*y[0] +x[1]*y[1]+...
 *
 * @param x
 * @param y
 * @return
 */
float dot (genType x, genType y);

/*
 * Returns the cross product of x and y, i.e.
 * [x[1]*y[2]?y[1]*x[2],
 *  x[2]*y[0]?y[2]*x[0],
 *  x[0]*?y[1]?y[0]*x[1]]
 *
 * @param x
 * @param y
 * @return
 */
vec3 cross (vec3 x, vec3 y);

/*
 * Returns a vector in the same direction as x but with a length of 1.
 *
 * @param x
 * @return
 */
genType normalize (genType x);

/*
 * If dot(Nref, I) < 0 return N, otherwise return –N.
 *
 * @param N
 * @param I
 * @param Nref
 * @return
 */
genType faceforward(genType N, genType I, genType Nref);

/*
 * For the incident vector I and surface orientation N, returns the reflection direction:
 *      I – 2 * dot(N, I) * N
 * N must already be normalized in order to achieve the desired result.
 *
 * @param I
 * @param N
 * @return
 */
genType reflect (genType I, genType N);

/*
 * For the incident vector I and surface normal N, and the ratio of indices of refraction eta, return the refraction
 * vector. The result is computed by
 *
 *  k = 1.0 - eta * eta * (1.0 - dot(N, I) * dot(N, I))
 *  if (k < 0.0)
 *      return genType(0.0)
 *  else
 *      return eta * I - (eta * dot(N, I) + sqrt(k)) * N
 *
 * The input parameters for the incident vector I and the surface normal N must already be normalized to get the
 * desired results.
 *
 * @param I
 * @param N
 * @param eta
 * @return
 */
genType refract(genType I, genType N, float eta);


//*** Matrix Functions ***//

/*
 * Multiply matrix x by matrix y component-wise, i.e., result[i][j] is the scalar product of x[i][j] and y[i][j].
 * Note: to get linear algebraic matrix multiplication, use the multiply operator (*).
 *
 * @param x
 * @param y
 * @return
 */
mat matrixCompMult (mat x, mat y);

/*
 * Treats the first parameter c as a column vector (matrix with one column) and the second parameter r as a row
 * vector (matrix with one row) and does a linear algebraic matrix multiply c * r, yielding a matrix whose number of
 * rows is the number of components in c and whose number of columns is the number of components in r.
 *
 * @param c
 * @param r
 * @return
 */
mat2 outerProduct(vec2 c, vec2 r);
mat3 outerProduct(vec3 c, vec3 r);
mat4 outerProduct(vec4 c, vec4 r);
mat2x3 outerProduct(vec3 c, vec2 r);
mat3x2 outerProduct(vec2 c, vec3 r);
mat2x4 outerProduct(vec4 c, vec2 r);
mat4x2 outerProduct(vec2 c, vec4 r);
mat3x4 outerProduct(vec4 c, vec3 r);
mat4x3 outerProduct(vec3 c, vec4 r);

/*
 * Returns a matrix that is the transpose of m. The input matrix m is not modified.
 *
 * @param m
 * @return
 */
mat2 transpose(mat2 m);
mat3 transpose(mat3 m);
mat4 transpose(mat4 m);
mat2x3 transpose(mat3x2 m);
mat3x2 transpose(mat2x3 m);
mat2x4 transpose(mat4x2 m);
mat4x2 transpose(mat2x4 m);
mat3x4 transpose(mat4x3 m);
mat4x3 transpose(mat3x4 m);



//*** Vector Relational Functions ***//

/*
 * Returns the component-wise compare of x < y.
 *
 * @param x
 * @param y
 * @return
 */
bvec lessThan(vec x, vec y);
bvec lessThan(ivec x, ivec y);

/*
 * Returns the component-wise compare of x <= y.
 *
 * @param x
 * @param y
 * @return
 */
bvec lessThanEqual(vec x, vec y);
bvec lessThanEqual(ivec x, ivec y);

/*
 * Returns the component-wise compare of x > y.
 *
 * @param x
 * @param y
 * @return
 */
bvec greaterThan(vec x, vec y);
bvec greaterThan(ivec x, ivec y);

/*
 * Returns the component-wise compare of x >= y.
 *
 * @param x
 * @param y
 * @return
 */
bvec greaterThanEqual(vec x, vec y);
bvec greaterThanEqual(ivec x, ivec y);

/*
 * Returns the component-wise compare of x == y.
 *
 * @param x
 * @param y
 * @return
 */
bvec equal(vec x, vec y);
bvec equal(ivec x, ivec y);
bvec equal(bvec x, bvec y);

/*
 * Returns the component-wise compare of x != y.
 *
 * @param x
 * @param y
 * @return
 */
bvec notEqual(vec x, vec y);
bvec notEqual(ivec x, ivec y);
bvec notEqual(bvec x, bvec y);


/*
 * Returns true if any component of x is true.
 *
 * @param x
 * @return
 */
bool any(bvec x);

/*
 * Returns true only if all components of x are true.
 *
 * @param x
 * @return
 */
bool all(bvec x);

/*
 *  Returns the component-wise logical complement of x.
 *
 * @param x
 * @return
 */
bvec not(bvec x);



//*** Texture Lookup Functions ***//

/*
 * Use the texture coordinate coord to do a texture lookup in the 1D texture currently
 * bound to sampler. For the projective (“Proj”) versions, the texture coordinate
 * coord.s is divided by the last component of coord.
 *
 * @param sampler
 * @param coord
 * @param bias
 * @param lod
 * @return
 */
vec4 texture1D (sampler1D sampler, float coord);
vec4 texture1D (sampler1D sampler, float coord, float bias );
vec4 texture1DProj (sampler1D sampler, vec2 coord);
vec4 texture1DProj (sampler1D sampler, vec2 coord, float bias);
vec4 texture1DProj (sampler1D sampler, vec4 coord);
vec4 texture1DProj (sampler1D sampler, vec4 coord, float bias);
vec4 texture1DLod (sampler1D sampler, float coord, float lod);
vec4 texture1DProjLod (sampler1D sampler, vec2 coord, float lod);
vec4 texture1DProjLod (sampler1D sampler, vec4 coord, float lod);

/*
 * Use the texture coordinate coord to do a texture lookup in the 2D texture currently
 * bound to sampler. For the projective (“Proj”) versions, the texture coordinate
 * (coord.s, coord.t) is divided by the last component of coord. The third component
 * of coord is ignored for the vec4 coord variant.
 *
 * @param sampler
 * @param coord
 * @param bias
 * @param lod
 * @return
 */
vec4 texture2D (sampler2D sampler, vec2 coord);
vec4 texture2D (sampler2D sampler, vec2 coord, float bias);
vec4 texture2DProj (sampler2D sampler, vec3 coord);
vec4 texture2DProj (sampler2D sampler, vec3 coord, float bias);
vec4 texture2DProj (sampler2D sampler, vec4 coord);
vec4 texture2DProj (sampler2D sampler, vec4 coord, float bias);
vec4 texture2DLod (sampler2D sampler, vec2 coord, float lod);
vec4 texture2DProjLod (sampler2D sampler,vec3 coord, float lod);
vec4 texture2DProjLod (sampler2D sampler, vec4 coord, float lod);

/*
 * Use the texture coordinate coord to do a texture lookup in the 3D texture currently
 * bound to sampler. For the projective (“Proj”) versions, the texture coordinate is
 * divided by coord.q.
 *
 * @param sampler
 * @param coord
 * @param bias
 * @param lod
 * @return
 */
vec4 texture3D (sampler3D sampler, vec3 coord);
vec4 texture3D (sampler3D sampler, vec3 coord, float bias);
vec4 texture3DProj (sampler3D sampler, vec4 coord);
vec4 texture3DProj (sampler3D sampler, vec4 coord, float bias);
vec4 texture3DLod (sampler3D sampler, vec3 coord, float lod);
vec4 texture3DProjLod (sampler3D sampler, vec4 coord, float lod);

/*
 * Use the texture coordinate coord to do a texture lookup in the cube map texture
 * currently bound to sampler. The direction of coord is used to select which face to do a 2-
 * dimensional texture lookup in, as described in section 3.8.6 in version 1.4 of the OpenGL specification.
 *
 * @param sampler
 * @param coord
 * @param bias
 * @param lod
 * @return
 */
vec4 textureCube (samplerCube sampler, vec3 coord);
vec4 textureCube (samplerCube sampler, vec3 coord, float bias);
vec4 textureCubeLod (samplerCube sampler, vec3 coord, float lod);

/*
 * Use texture coordinate coord to do a depth comparison lookup on the depth texture
 * bound to sampler, as described in section 3.8.14 of version 1.4 of the OpenGL
 * specification. The 3rd component of coord (coord.p) is used as the R value. The texture
 * bound to sampler must be a depth texture, or results are undefined. For the projective
 * (“Proj”) version of each built-in, the texture coordinate is divide by coord.q, giving a
 * depth value R of coord.p/coord.q. The second component of coord is ignored for the “1D” variants.
 *
 * @param sampler
 * @param coord
 * @param bias
 * @param lod
 * @return
 */
vec4 shadow1D (sampler1DShadow sampler, vec3 coord);
vec4 shadow1D (sampler1DShadow sampler, vec3 coord, float bias);
vec4 shadow2D (sampler2DShadow sampler, vec3 coord);
vec4 shadow2D (sampler2DShadow sampler, vec3 coord, float bias);
vec4 shadow1DProj (sampler1DShadow sampler, vec4 coord);
vec4 shadow1DProj (sampler1DShadow sampler, vec4 coord, float bias);
vec4 shadow2DProj (sampler2DShadow sampler, vec4 coord);
vec4 shadow2DProj (sampler2DShadow sampler, vec4 coord, float bias);
vec4 shadow1DLod (sampler1DShadow sampler, vec3 coord, float lod);
vec4 shadow2DLod (sampler2DShadow sampler, vec3 coord, float lod);
vec4 shadow1DProjLod(sampler1DShadow sampler, vec4 coord, float lod);
vec4 shadow2DProjLod(sampler2DShadow sampler, vec4 coord, float lod);



//*** Noise Functions ***//

/*
 * Returns a 1D noise value based on the input value x.
 * MOST LIKELY NOT IMPLEMENTED IN HARDWARE!
 *
 * @param x
 * @return
 */
float noise1 (genType x);

/*
 * Returns a 2D noise value based on the input value x.
 * MOST LIKELY NOT IMPLEMENTED IN HARDWARE!
 *
 * @param x
 * @return
 */
vec2 noise2 (genType x);

/*
 * Returns a 3D noise value based on the input value x.
 * MOST LIKELY NOT IMPLEMENTED IN HARDWARE!
 *
 * @param x
 * @return
 */
vec3 noise3 (genType x);

/*
 * Returns a 4D noise value based on the input value x.
 * MOST LIKELY NOT IMPLEMENTED IN HARDWARE!
 *
 * @param x
 * @return
 */
vec4 noise4 (genType x);
