// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "Otherwise, if there is a default label, execution will continue after
//     that label....It is an error to have more than one default or a
//     replicated constant-expression....Fall through labels are allowed, but
//     it is an error to have no statement between a label and the end of the
//     switch statement."

#version 130

void main() {
   int tmp = 0;
   switch (1) {
   default:
      tmp = 1;
   }

   gl_Position = vec4(0.0);
}
