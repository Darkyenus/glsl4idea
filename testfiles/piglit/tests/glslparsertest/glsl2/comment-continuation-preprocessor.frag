// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]
//
// GLSL specification states that
//
//   Removal of new lines by the line-continuation character ( \ ) logically
//   occurs before comments are processed.
//
// So, in the shader below, the first // in the macro below effectively makes
// the rest of the macro's characters go away, and hence, produces a
// compilation error.
//
// GNU cpp does the same. However NVIDIA accepts the shader.
//
// This was seen with MindCAD 3D Viewer.
//

#define DECLARE_FOO()                                                      \
    void foo(out vec4 color)                                               \
    {                                                                      \
        color = vec4(0, 1, 0, 1);                                          \
                                                                           \
//      if (0)                                                             \
//      {                                                                  \
//           color = vec4(0, 0, 1, 1) ;                                    \
//      }                                                                  \
    }

DECLARE_FOO()

void main()
{
     foo(gl_FragColor);
}

