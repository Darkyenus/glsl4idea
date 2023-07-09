#version 330

in int someValue;

out vec4 fragColor;

void main() {
    switch (someValue) {
        case 0:
            // Should be indented acoording to setting
            fragColor = vec4(1.0, 0.0, 0.0, 1.0);
            break;
        case 1:
            // Should be indented acoording to setting
            vec4 a = vec4(1.0, 0.0, 0.0, 1.0);
            vec4 b = vec4(0.0, 1.0, 0.0, 1.0);
            fragColor = a + b;
            break;
        case 2:// Don't do anything here
        default :// Same as above
    }
}

void switchAtEOFWithBlock() {
switch (someValue) {
case 0:
fragColor = vec4(1.0, 0.0, 0.0, 1.0);// Error here
