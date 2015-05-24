// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* The program should terminate with an error message and not get into an
   infinite loop */
#ifdef name

void main()
{
   gl_FragColor = vec4(1);
}
