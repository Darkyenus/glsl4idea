// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "Conditionals can be nested."

#version 130

void main() {
   switch (1) {
   case 0:
      switch (0) {
      }
   }

   gl_Position = vec4(0.0);
}
