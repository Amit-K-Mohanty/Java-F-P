import java.util.function.Consumer;

class C_ConsumerTest {
    public void consumer1() {
        StringBuilder sb = new StringBuilder("xyz");
        Consumer<StringBuilder> appenderConsumer = builder -> builder.append("abc");

        appenderConsumer.accept(sb);
        assert "xyzabc".equals(sb.toString());

    }
    
}
