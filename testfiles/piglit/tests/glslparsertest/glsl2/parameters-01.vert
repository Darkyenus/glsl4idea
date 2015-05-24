// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL: redefinition of a() */

void a()
{
	;	
}

void a()
{
	;	
}
