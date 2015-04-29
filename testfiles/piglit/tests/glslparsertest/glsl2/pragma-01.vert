// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
#pragma optimize(on)
#pragma debug(off)

void main()
{
  gl_Position = gl_Vertex;
}

#pragma debug(on)
#pragma optimize(off)
