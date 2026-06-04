#version 140 // interface blocks were added in GL3.1 (GLSL 140)

// these should pass:

uniform Camera {
    layout(column_major) mat4 projection, view;
    uniform vec2 depthRange;
} camera[4];

patch in Patch {
    vec4 position = vec4(0.0);
    flat vec2 colour;
};

layout(binding = 3, std140) buffer Data {
    uint element[128];
} data[128][128];

layout(points) in;

layout(line_strip, max_vertices = 6) out;

// these should fail:

uniform EmptyBlock {
};

in BlockWithInitializer {
    int x;
} block = { 3 };

buffer BlockWithOpaqueType {
    sampler2D sampler;
};
