// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL
 *
 * From page 17 (page 23 of the PDF) of the GLSL 1.20 spec:
 *
 *    "Samplers cannot be treated as l-values; hence cannot be used as
 *     out or inout function parameters, nor can they be assigned into."
 */

uniform sampler3D volShadSampler0;

struct VolShad
{
    sampler3D texture;
    int       samples;
    int       channels;
    mat4      worldToScreen;
};

vec3 testfunc(VolShad vs, vec3 p)
{
    return vec3(1.0, 1.0, 1.0);
}

void main()
{
  VolShad volShad0;

  volShad0.texture = volShadSampler0;
  volShad0.samples = 8;
  volShad0.channels = 3;
  volShad0.worldToScreen = mat4(0.987538, 0.9114460, 0.6269080, 0.6269080,
				0.000000, 2.2036100, -0.496881, -0.496880,
				1.031690, -0.872442, -0.600081, -0.600081,
				-47.4917, 35.483100, 75.264900, 75.364800);

    vec3 outputColor = testfunc(volShad0, vec3(1, 1, 1));
    gl_FragColor = vec4(outputColor, 1);
}
