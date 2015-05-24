// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that this function-parameter qualifier sequence is invalid:
// precision-qualifier parameter-qualifier
//
// From section 4.7 of the GLSL 1.30 spec:
//     When multiple qualifications are present, they must follow a strict
//     order. This order is as follows.
//         invariant-qualifier interpolation-qualifier storage-qualifier precision-qualifier
//         storage-qualifier parameter-qualifier precision-qualifier

#version 130


float f(highp in float x) {
	return 0.0;
}
