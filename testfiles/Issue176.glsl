#version 120

int64_t min_v(i64vec2 value){
    return min(value.x, value.y);
}

void main() {
    int64_t number = min_v(i64vec2(12, 42));
}
