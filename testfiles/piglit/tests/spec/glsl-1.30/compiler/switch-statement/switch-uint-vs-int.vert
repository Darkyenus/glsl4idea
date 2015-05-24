// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 47 (page 51 of the PDF) of the GLSL 1.30 spec:
//
//     "The equality operators equal (==), and not equal (!=) operate
//     on all types. They result in a scalar Boolean. If the operand
//     types do not match, then there must be a conversion from Section
//     4.1.10 "Implicit Conversions" applied to one operand that can
//     make them match, in which case this conversion is done."
//
// From page 55 (Page 61 of the PDF) of the GLSL 1.30 spec:
//
//     "The type of init-expression in a switch statement must be a
//     scalar integer."
//
// From page 25 (Page 31 of the PDF) of the GLSL 1.30 spec:
//
//     "There are no implicit conversions between signed and unsigned
//     integers."
//

#version 130

void main() {
   uint x = 1u;
   switch (x) {
   case 1:
      break;
   default:
      break;
   }
   gl_Position = vec4(0.0);
}
