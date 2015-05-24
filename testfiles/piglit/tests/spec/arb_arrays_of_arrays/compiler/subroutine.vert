/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_arrays_of_arrays GL_ARB_shader_subroutine GL_ARB_gpu_shader5
 * [end config]
 */
#version 150
#extension GL_ARB_arrays_of_arrays: enable
#extension GL_ARB_shader_subroutine: enable
#extension GL_ARB_gpu_shader5: enable

subroutine vec4[3][2] basicSubRoutine();

subroutine (basicSubRoutine) vec4[3][2] option1() {
  vec4 a[3][2] = vec4[3][2](vec4[2](vec4(0.0), vec4(1.0)),
                            vec4[2](vec4(0.0), vec4(1.0)),
                            vec4[2](vec4(0.0), vec4(1.0)));
  return a;
}

subroutine (basicSubRoutine) vec4[3][2] option2() {
  vec4 a[3][2] = vec4[3][2](vec4[2](vec4(1.0), vec4(0.0)),
                            vec4[2](vec4(1.0), vec4(0.0)),
                            vec4[2](vec4(1.0), vec4(0.0)));
  return a;
}

subroutine uniform basicSubRoutine subRoutineSelection;

void main()
{
  vec4[3][2] a = subRoutineSelection();

  gl_Position = a[0][0];
}
