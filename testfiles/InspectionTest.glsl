//Inspection test file. Inspections should create problem descriptors only where noted.

struct Test {
    vec3 v;
};
void CStyleConstructorInspectionTest() {
    vec3 v = {1, 2, 3};
    Test t1 = {vec3(0)};//Should warn here
    Test[] t2 = {Test(vec3(1.0))};
}