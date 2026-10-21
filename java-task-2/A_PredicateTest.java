import java.util.function.Predicate;

class A_PredicateTest{
    public void a_predicate1(){
        Predicate<String> pred = value -> value.length() >= 4;

        assert pred.test("abcde");
        assert !pred.test("abcd");
        assert !pred.test("");
    }  
}
