// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform int count;
uniform vec4 data[10];

void main()
{
  vec4 value = vec4(0.0);

  int i = 0;
  do {
    value += data[i];
    i++;
  } while (i < count);

  gl_Position = value;
}
