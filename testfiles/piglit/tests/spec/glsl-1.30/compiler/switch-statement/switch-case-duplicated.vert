// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "It is an error to have more than one default or a replicated
//      constant-expression."

#version 130

void main() {
   int tmp = 0;
   switch (1) {
   case 0:
   case 0:
      tmp = 1;
   }

   gl_Position = vec4(0.0);
}
