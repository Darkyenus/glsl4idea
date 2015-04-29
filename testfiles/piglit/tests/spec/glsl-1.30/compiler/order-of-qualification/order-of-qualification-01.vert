// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that this qualifier sequence is valid:
// invariant-qualifier interpolation-qualifier storage-qualifier precision qualifier
//
// From section 4.7 of the GLSL 1.30 spec:
//     When multiple qualifications are present, they must follow a strict
//     order. This order is as follows.
//         invariant-qualifier interpolation-qualifier storage-qualifier precision-qualifier
//         storage-qualifier parameter-qualifier precision-qualifier

#version 130

invariant smooth centroid out highp float x;

float f() {
	return 0.0;
}
