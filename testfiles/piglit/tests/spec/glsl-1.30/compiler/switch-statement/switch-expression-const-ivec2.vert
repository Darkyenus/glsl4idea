// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "The type of init-expression in a switch statement must be a scalar
//     integer."

#version 130

void main() {
   switch (ivec2(0, 0)) {
   }

   gl_Position = vec4(0.0);
}
