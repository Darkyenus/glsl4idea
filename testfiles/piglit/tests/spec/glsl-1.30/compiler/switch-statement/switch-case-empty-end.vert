// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "Fall through labels are allowed, but it is an error to have no
//     statement between a label and the end of the switch statement."
 
#version 130

void main() {
   switch (1) {
   case 0:
   }

   gl_Position = vec4(0.0);
}
