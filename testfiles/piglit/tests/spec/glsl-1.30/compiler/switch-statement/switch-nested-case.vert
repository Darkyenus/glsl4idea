// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 57 (page 63 of the PDF) of the GLSL 1.30 spec:
//
//     "No case or default labels can be nested inside other flow control
//     nested within their corresponding switch."

#version 130

void main() {
   int tmp = 0;
   switch (1) {
   case 0:
      while (1) {
         case 1:
	 tmp = 1;
      }
   }

   gl_Position = vec4(0.0);
}
