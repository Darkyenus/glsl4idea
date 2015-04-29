// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */

struct str {
  float params[4];
};

void main()
{
  str s;

  for (int i = 0; i < 4; ++i)
    s.params[i] = 1.0;

  gl_FragColor = vec4(s.params[0], s.params[1],
		      s.params[2], s.params[3]);
}
