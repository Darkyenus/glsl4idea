// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL */
/* NVIDIA - incompatible types in initialization */
/* fglrx - Undeclared identifier x_adjust */
/* Apple - 'x_adjust' : undeclared identifier */


varying vec4 pos;

vec4 x_adjust(vec4 pos)
{
  if (pos.x > 0.0)
    return vec4(-0.5, 0.0, 0.0, 0.0);
  else
    return vec4(1.5, 0.0, 0.0, 0.0);
}

vec4 y_adjust(vec4 pos)
{
  vec4 x = x_adjust;

  if (pos.y > 0.0)
    return vec4(0.0, -0.5, 0.5, 0.0) + x;
  else
    return vec4(0.0, 1.5, 0.5, 0.0) + x;
}

void main()
{
  gl_FragColor = sign(pos) + y_adjust(pos);
}
