/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "When an array size is specified in a declaration, it must be an
 *     integral constant expression (see Section 4.3.3 "Constant Expressions")
 *     greater than zero."
 */
#version 120

const vec4 v4_1 = vec4(1);
const vec4 v4_0 = v4_1 - v4_1;
const vec3 v3_1 = vec3(1);
const vec3 v3_0 = v3_1 - v3_1;
const vec2 v2_1 = vec2(1);
const vec2 v2_0 = v2_1 - v2_1;

const ivec4 iv4_1 = ivec4(1);
const ivec4 iv4_0 = iv4_1 - iv4_1;
const ivec3 iv3_1 = ivec3(1);
const ivec3 iv3_0 = iv3_1 - iv3_1;
const ivec2 iv2_1 = ivec2(1);
const ivec2 iv2_0 = iv2_1 - iv2_1;

const bvec4 bv4_1 = bvec4(true);
const bvec4 bv4_0 = not(bv4_1);
const bvec3 bv3_1 = bvec3(true);
const bvec3 bv3_0 = not(bv3_1);
const bvec2 bv2_1 = bvec2(true);
const bvec2 bv2_0 = not(bv2_1);

// All forms of lessThan.
float [all(    lessThan( v4_1,  v4_0) ) ? -1 :  1] array01;
float [all(not(lessThan( v4_1,  v4_0))) ?  1 : -1] array02;
float [any(    lessThan( v4_1,  v4_0) ) ? -1 :  1] array03;
float [any(not(lessThan( v4_1,  v4_0))) ?  1 : -1] array04;
float [all(    lessThan(iv4_1, iv4_0) ) ? -1 :  1] array05;
float [all(not(lessThan(iv4_1, iv4_0))) ?  1 : -1] array06;
float [any(    lessThan(iv4_1, iv4_0) ) ? -1 :  1] array07;
float [any(not(lessThan(iv4_1, iv4_0))) ?  1 : -1] array08;

// All forms of lessThanEqual.
float [all(    lessThanEqual( v4_1,  v4_0) ) ? -1 :  1] array11;
float [all(not(lessThanEqual( v4_1,  v4_0))) ?  1 : -1] array12;
float [any(    lessThanEqual( v4_1,  v4_0) ) ? -1 :  1] array13;
float [any(not(lessThanEqual( v4_1,  v4_0))) ?  1 : -1] array14;
float [all(    lessThanEqual(iv4_1, iv4_0) ) ? -1 :  1] array15;
float [all(not(lessThanEqual(iv4_1, iv4_0))) ?  1 : -1] array16;
float [any(    lessThanEqual(iv4_1, iv4_0) ) ? -1 :  1] array17;
float [any(not(lessThanEqual(iv4_1, iv4_0))) ?  1 : -1] array18;

// All forms of greaterThan.
float [all(    greaterThan( v4_1,  v4_0) ) ?  1 : -1] array21;
float [all(not(greaterThan( v4_1,  v4_0))) ? -1 :  1] array22;
float [any(    greaterThan( v4_1,  v4_0) ) ?  1 : -1] array23;
float [any(not(greaterThan( v4_1,  v4_0))) ? -1 :  1] array24;
float [all(    greaterThan(iv4_1, iv4_0) ) ?  1 : -1] array25;
float [all(not(greaterThan(iv4_1, iv4_0))) ? -1 :  1] array26;
float [any(    greaterThan(iv4_1, iv4_0) ) ?  1 : -1] array27;
float [any(not(greaterThan(iv4_1, iv4_0))) ? -1 :  1] array28;

// All forms of greaterThanEqual.
float [all(    greaterThanEqual( v4_1,  v4_0) ) ?  1 : -1] array31;
float [all(not(greaterThanEqual( v4_1,  v4_0))) ? -1 :  1] array32;
float [any(    greaterThanEqual( v4_1,  v4_0) ) ?  1 : -1] array33;
float [any(not(greaterThanEqual( v4_1,  v4_0))) ? -1 :  1] array34;
float [all(    greaterThanEqual(iv4_1, iv4_0) ) ?  1 : -1] array35;
float [all(not(greaterThanEqual(iv4_1, iv4_0))) ? -1 :  1] array36;
float [any(    greaterThanEqual(iv4_1, iv4_0) ) ?  1 : -1] array37;
float [any(not(greaterThanEqual(iv4_1, iv4_0))) ? -1 :  1] array38;

// All forms of equal.
float [all(    equal( v4_1,  v4_0) ) ? -1 :  1] array41;
float [all(not(equal( v4_1,  v4_0))) ?  1 : -1] array42;
float [any(    equal( v4_1,  v4_0) ) ? -1 :  1] array43;
float [any(not(equal( v4_1,  v4_0))) ?  1 : -1] array44;
float [all(    equal(iv4_1, iv4_0) ) ? -1 :  1] array45;
float [all(not(equal(iv4_1, iv4_0))) ?  1 : -1] array46;
float [any(    equal(iv4_1, iv4_0) ) ? -1 :  1] array47;
float [any(not(equal(iv4_1, iv4_0))) ?  1 : -1] array48;
float [all(    equal(bv4_1, bv4_0) ) ? -1 :  1] array49;
float [all(not(equal(bv4_1, bv4_0))) ?  1 : -1] array4a;
float [any(    equal(bv4_1, bv4_0) ) ? -1 :  1] array4b;
float [any(not(equal(bv4_1, bv4_0))) ?  1 : -1] array4c;

// All forms of notEqual.
float [all(    notEqual( v4_1,  v4_0) ) ?  1 : -1] array51;
float [all(not(notEqual( v4_1,  v4_0))) ? -1 :  1] array52;
float [any(    notEqual( v4_1,  v4_0) ) ?  1 : -1] array53;
float [any(not(notEqual( v4_1,  v4_0))) ? -1 :  1] array54;
float [all(    notEqual(iv4_1, iv4_0) ) ?  1 : -1] array55;
float [all(not(notEqual(iv4_1, iv4_0))) ? -1 :  1] array56;
float [any(    notEqual(iv4_1, iv4_0) ) ?  1 : -1] array57;
float [any(not(notEqual(iv4_1, iv4_0))) ? -1 :  1] array58;
float [all(    notEqual(bv4_1, bv4_0) ) ?  1 : -1] array59;
float [all(not(notEqual(bv4_1, bv4_0))) ? -1 :  1] array5a;
float [any(    notEqual(bv4_1, bv4_0) ) ?  1 : -1] array5b;
float [any(not(notEqual(bv4_1, bv4_0))) ? -1 :  1] array5c;

void main()
{
  gl_Position = vec4(array01.length()
		     + array02.length()
		     + array03.length()
		     + array04.length()
		     + array05.length()
		     + array06.length()
		     + array07.length()
		     + array08.length()
		     + array11.length()
		     + array12.length()
		     + array13.length()
		     + array14.length()
		     + array15.length()
		     + array16.length()
		     + array17.length()
		     + array18.length()
		     + array21.length()
		     + array22.length()
		     + array23.length()
		     + array24.length()
		     + array25.length()
		     + array26.length()
		     + array27.length()
		     + array28.length()
		     + array31.length()
		     + array32.length()
		     + array33.length()
		     + array34.length()
		     + array35.length()
		     + array36.length()
		     + array37.length()
		     + array38.length()
		     + array41.length()
		     + array42.length()
		     + array43.length()
		     + array44.length()
		     + array45.length()
		     + array46.length()
		     + array47.length()
		     + array48.length()
		     + array49.length()
		     + array4a.length()
		     + array4b.length()
		     + array4c.length()
		     + array51.length()
		     + array52.length()
		     + array53.length()
		     + array54.length()
		     + array55.length()
		     + array56.length()
		     + array57.length()
		     + array58.length()
		     + array59.length()
		     + array5a.length()
		     + array5b.length()
		     + array5c.length());
}
