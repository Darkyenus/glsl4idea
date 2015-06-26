#version 120

void main() {
	int a = 1;
	int b = a >> 1;
	a <<= 3;
	int c = a | b;
	c = a & b & c;
	int d = ~c;

	ivec2 e = ivec2(5);
	e = e >> d;
}
