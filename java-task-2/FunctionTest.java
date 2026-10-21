import java.util.function.Function;

class B_FunctionTest {

public void function1(){
    Function<String, String> func = value -> "(" + value + ")";

    assert "(abc)".equals(func.apply("abc"));
    assert "()".equals(func.apply(""));
    }  
}