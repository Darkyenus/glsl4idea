// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS
 *
 * Test submitted by Thomas Jones (thomas.jones 'at' utoronto 'dot' ca) on
 * mesa-dev mailing list.
 */

#ifdef FOO
#if FOO == 4
#elif SAMP == 5
#endif
#endif
void main()  { gl_FragData[0] = vec4(0);}
