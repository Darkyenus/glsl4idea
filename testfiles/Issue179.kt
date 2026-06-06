//language=glsl
val glsl = """
#version 450
layout(local_size_x=1) in;
layout(rgba32f) uniform writeonly image2DArray writeTex;
uniform int numLayers;
void main() {
    vec4 pink = vec4(1.0, 0.753, 0.796, 1.0);
    
    for (int layer=0; layer<numLayers; layer++) {
        ivec3 coords = ivec3(gl_GlobalInvocationID.x, 
                             gl_GlobalInvocationID.y, 
                             layer);
        float alpha = sin(coords.x * 0.1 + layer) * 0.5 + 0.5;
        imageStore(writeTex, coords, pink * alpha);
    }
}
"""